import executeRequest from "../sender";
import {useEffect, useState} from "react";

export default function useUserSearch(searchName, pageNumber) {
    const [loading, setLoading] = useState(true)
    const [empty, setEmpty] = useState(false)
    const [content, setContent] = useState([])
    const [hasMore, setHasMore] = useState(false)

    useEffect(() => {
        setContent([])
    }, [searchName])

    useEffect(() => {
        setLoading(true)
        let url = `http://localhost:8082/gift-certificate-rest/api/v1/users/search?sortBy=createDate&sortType=desc&page=${pageNumber}&size=15`
        if (searchName != null) url += `&username=${searchName}`
        executeRequest(url, 'GET', null, {}, true)
            .catch( (error) => {
                // console.log(error)
                // connectionError()
            })
            .then((data) => {
                if (data.errorCode === 40005) {
                    setHasMore(false)
                    setLoading(false)
                } else {
                    localStorage.setItem('data', JSON.stringify(data))
                    setLoading(false)
                    setHasMore(true)
                    setContent(prevState => {
                        return prevState.concat(data)
                    })
                    // createPage(data)
                }
            })
    }, [searchName, pageNumber])

    return {loading, empty, content, hasMore}
}