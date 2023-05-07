import ReactDOM from "react-dom/client";
import React, {useEffect, useState} from "react";
import executeRequest from "./sender";
import {useLocalStorage} from "../js/hooks/useLocalStorage"

function Userbar(props) {
    const [username, setUsername] = useLocalStorage('', "user")
    const id = localStorage.getItem('currentUser')

    if (id !== undefined && id !== null) {
        useEffect(() => {
            const fetchData = async () => {
                const result = await executeRequest(`http://localhost:8082/gift-certificate-rest/api/v1/users/${id}`, 'GET', null, {}, true)
                if (result.errorCode === 40401) {
                    setUsername('')
                } else {
                    setUsername(result.username)
                }
            }

            fetchData()
        }, [])
    }

    const handleSignoutClick = () => {
        setUsername('')
        localStorage.setItem("userID", null)
        localStorage.setItem("admin", JSON.stringify(false))
        localStorage.setItem('accessToken', "")
        localStorage.setItem('refreshToken', "")
        window.location.href = "/"
    }

    return (
        <div className="header__menu-list">
            {username !== '' ?
                <>
                    <span className="menu-list__elem">{username}</span>
                    <a href="/cart.html" className="menu-list__elem search__dropbutton">Заказы</a>
                    <a href="/favs.html" className="menu-list__elem search__dropbutton">Избранное</a>
                    <button className="menu-list__elem search__dropbutton" onClick={handleSignoutClick}>Выход</button>
                </>
                :
                <>
                    <li className="menu-list__elem"><a className="menu-list__elem-link" href="login.html">Вход</a></li>
                    <li className="menu-list__elem"><a className="menu-list__elem-link" href="signup.html">Регистрация</a></li>
                </>
            }
        </div>
    )
}

const root = ReactDOM.createRoot(document.getElementById('userbar-container'))
root.render(<Userbar />)