import React, {useState} from "react";
import ReactDOM from "react-dom/client";
import executeRequest from "./sender.js"
import { host } from "./host-const.js"

function parseJwt (token) {
    let base64Url = token.split('.')[1];
    let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    let jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}

function Login(props) {
    const [login, setLogin] = useState('')
    const [password, setPassword] = useState('')
    const [loginError, setLoginError] = useState('')
    const [passwordError, setPasswordError] = useState('')

    const handleFormSubmit = (e) => {
        e.preventDefault()
        if (loginError === '' && passwordError === '') {
            let url = `http://${host}/housing/user/login`

            const details = {
                username: login,
                password: password
            }

            let formBody = [];
            for (let property in details) {
                let encodedKey = encodeURIComponent(property);
                let encodedValue = encodeURIComponent(details[property]);
                formBody.push(encodedKey + "=" + encodedValue);
            }
            formBody = formBody.join("&");

            executeRequest(url, 'POST', formBody, {
                'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
                'Accept-language': 'en'
            }, false)
                .then((data) => {
                    if (data.errorCode === 40104) {
                        setLoginError(data.errorMessage)
                    } else {
                        const accessToken = data.accessToken
                        setLoginError('')
                        localStorage.setItem('accessToken', accessToken)
                        localStorage.setItem('refreshToken', data.refreshToken)
                        document.location.href = '/'
                        localStorage.setItem('user', JSON.stringify(data.email))
                        localStorage.setItem('userID', JSON.stringify(data.userId))
                        if (parseJwt(accessToken).roles.includes("Admin")) {
                            localStorage.setItem("admin", JSON.stringify(true))
                        }
                    }
                })
        }
    }

    const handleLoginChange = (e) => {
        let text = e.target.value
        setLoginError('')
        if (text.length === 0)
            setLoginError('Username shouldn\'t be empty')
        setLogin(text)
    }

    const handlePasswordChange = (e) => {
        let text = e.target.value
        setLoginError('')
        if (text.length === 0)
            setPasswordError('Password shouldn\'t be empty')
        else
            setPasswordError('')
        setPassword(text)
    }

    return (
        <form onSubmit={handleFormSubmit} className="login-form">
            <a className="login-form__backlink" href="/">
                <div className="login-form__backlink-container">
                    <img className="login-logo" src="assets/img/giftbox.png" alt="logo"/>
                    <span>Дома</span>
                </div>
            </a>
            <input onChange={handleLoginChange} value={login} className="login-form__input" type="text" placeholder="Логин"/>
            <span className={'error'}>{loginError}</span>
            <input onChange={handlePasswordChange} value={password} className="login-form__input" type="password" placeholder="Пароль"/>
            <span className={'error'}>{passwordError}</span>
            <button className="login-form__submit" type="submit">Войти</button>
        </form>
    )
}

const root = ReactDOM.createRoot(document.getElementById('root'))
root.render(<Login />)