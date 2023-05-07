import ReactDOM from "react-dom/client";
import React, {useState} from "react";
import executeRequest from "./sender";
import { host } from "./host-const"

function Signup(props) {
    const [login, setLogin] = useState('')
    const [password, setPassword] = useState('')
    const [confirmPassword, setConfirmPassword] = useState('')
    const [loginError, setLoginError] = useState('')
    const [passwordError, setPasswordError] = useState('')
    const [confirmPasswordError, setConfirmPasswordError] = useState('')
    const [name, setName] = useState('')
    const [lastName, setLastName] = useState('')
    const [phone, setPhone] = useState('')
    const [idNumber, setIiNumber] = useState('')

    const handleFormSubmit = (e) => {
        e.preventDefault()
        if (loginError === '' && passwordError === '' && confirmPasswordError === '') {
            let url = `http://${host}/housing/user/signup`

            const details = {
                email: login,
                password: password,
                phoneNumber: phone,
                firstName: name,
                lastName: lastName,
                passIdentificationNumber: idNumber
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
                    if (data.errorCode === 40902) {
                        setLoginError(data.errorMessage)
                    } else {
                        setLoginError('')
                        document.location.href = '/login.html'
                    }
                })
        }
    }

    const handleLoginChange = (e) => {
        let text = e.target.value
        setLoginError('')
        if (text.length < 3)
            setLoginError('Username should be at least 3 characters')
        if (text.length >= 30)
            setLoginError('Username should be less than 30 characters')
        setLogin(text)
    }

    const handlePasswordChange = (e) => {
        let text = e.target.value
        setPasswordError('')
        if (text.length < 4)
            setPasswordError('Password should be at least 4 characters')
        if (text.length >= 30)
            setPasswordError('Password should be less than 30 characters')
        setPassword(text)
    }

    const handleConfirmPasswordChange = (e) => {
        let text = e.target.value
        setConfirmPasswordError('')
        if (text !== password) setConfirmPasswordError('Passwords are not matching')
        setConfirmPassword(text)
    }

    return (
        <form onSubmit={handleFormSubmit} className="signup-form">
            <a className="signup-form__backlink" href="/">
                <div className="signup-form__backlink-container">
                    <img className="signup-logo" src="assets/img/giftbox.png" alt="logo"/>
                    <span>Дома</span>
                </div>
            </a>
            <input onChange={handleLoginChange} value={login} className="signup-form__input" type="text" placeholder="логин"/>
            <span className={'error'}>{loginError}</span>
            <input onChange={handlePasswordChange} value={password} className="signup-form__input" type="password" placeholder="Пароль"/>
            <span className={'error'}>{passwordError}</span>
            <input onChange={handleConfirmPasswordChange} value={confirmPassword} className="signup-form__input" type="password" placeholder="Подтвердите пароль"/>
            <input onChange={e => setName(e.target.value)} value={name} className="signup-form__input" type="text" placeholder="Имя" />
            <input onChange={e => setLastName(e.target.value)} value={lastName} className="signup-form__input" type="text" placeholder="Фамилия" />
            <input onChange={e => setPhone(e.target.value)} value={phone} className="signup-form__input" type="text" placeholder="Номер телефона" />
            <input onChange={e => setIiNumber(e.target.value)} value={idNumber} className="signup-form__input" type="text" placeholder="Идентификационный номер" />
            <span className={'error'}>{confirmPasswordError}</span>
            <button className="signup-form__submit" type="submit">Регистрация</button>
        </form>
    )
}

const root = ReactDOM.createRoot(document.getElementById('root'))
root.render(<Signup />)