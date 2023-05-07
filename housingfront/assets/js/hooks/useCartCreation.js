import {useEffect, useState} from "react";
import executeRequest from "../sender";
import { host } from "../host-const"

export default function useCartCreation(id) {
    const [empty, setEmpty] = useState(false)
    const [certificates, setCertificates] = useState([])

    useEffect(() => {
        const admin = JSON.parse(localStorage.getItem("admin"))
        let url;
        if (admin) {
            url = `http://${host}/housing/deal?page=0&size=10000000`
        } else {
            url = `http://${host}/housing/user/${id}/deals`
        }

        executeRequest(url, 'GET', null, {}, false)
            .catch((error) => {
                console.log(error)
            })
            .then((data) => {
                if (data.length === 0) {
                    setEmpty(true)
                } else {
                    setCertificates(data)
                }
            })
    }, [id])

    return {certificates, empty}
}