import {useEffect, useState} from "react";
import executeRequest from "../sender";

export default function useTagSearch(searchName, pageNumber, counter) {
    const [loading, setLoading] = useState(true)
    const [empty, setEmpty] = useState(false)
    const [content, setContent] = useState([])
    const [hasMore, setHasMore] = useState(false)

    useEffect(() => {
        setContent([])
    }, [searchName, counter])

    useEffect(() => {
        setLoading(true)
        let url = `http://localhost:8082/gift-certificate-rest/api/v1/tags/search?sortBy=createDate&sortType=desc&page=${pageNumber}&size=15`
        if (searchName != null) url += `&name=${searchName}`
        executeRequest(url, 'GET', null, {}, false)
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
    }, [searchName, pageNumber, counter])

    return {loading, empty, content, hasMore}
}