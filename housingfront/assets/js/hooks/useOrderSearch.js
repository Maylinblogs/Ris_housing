import {useEffect, useState} from "react";
import executeRequest from "../sender";

export default function useOrderSearch(pageNumber) {
    const [loading, setLoading] = useState(true)
    const [empty, setEmpty] = useState(false)
    const [content, setContent] = useState([])
    const [hasMore, setHasMore] = useState(false)

    useEffect(() => {
        setLoading(true)
        let url = `http://localhost:8082/gift-certificate-rest/api/v1/orders?page=${pageNumber}&size=10`
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
    }, [pageNumber])

    return {loading, empty, content, hasMore}
}