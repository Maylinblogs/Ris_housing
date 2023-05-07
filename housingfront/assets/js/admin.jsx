import ReactDOM from "react-dom/client";
import React, {useCallback, useEffect, useRef, useState} from "react";
import useCertificateSearch from "./hooks/useCertificateSearch";
import useTagSearch from "./hooks/useTagSearch";
import executeRequest from "./sender";
import useUserSearch from "./hooks/useUserSearch";
import useOrderSearch from "./hooks/useOrderSearch";
import {Tag} from "./admin-components/Tag.jsx"
import {Certificate} from "./admin-components/Certificate.jsx";
import {Order} from "./admin-components/Order.jsx";
import {User} from "./admin-components/User.jsx";
import {Modal} from "./admin-components/Modal.jsx";
import {ModalContentEdit} from "./admin-components/ModalContentEdit.jsx";
import {ModalContentAddCertificate} from "./admin-components/ModalContentAddCertificate.jsx";
import {ModalContentAddTag} from "./admin-components/ModalContentAddTag.jsx";

function parseJwt(token) {
    let base64Url = token.split('.')[1];
    let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    let jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}

function Table(props) {
    const [modalError, setModalError] = useState('')
    const [activeEdit, setModalActiveEdit] = useState(false)

    const [modalValue, setModalValue] = useState({})
    const [counter, setCounter] = useState(0)
    // const [deleted, setDeleted] = useState([])
    // const [hasMore, setHasMore] = useState(true)
    // const [loading, setLoading] = useState(false)

    function getLastElemRef(loading, hasMore) {
        const observer = useRef()
        return useCallback(node => {
            if (loading) return
            if (observer.current) observer.current.disconnect()
            observer.current = new IntersectionObserver(entries => {
                if (entries[0].isIntersecting && hasMore) {
                    props.setPage(prevPageNumber => prevPageNumber + 1)
                }
            })
            if (node) observer.current.observe(node)
        }, [loading, hasMore])
    }

    const handleSubmitAddCertificateClick = (name, desc, price, duration) => {
        const body = {
            "name": name,
            "description": desc,
            "price": price,
            "duration": duration,
            "tagSet": []
        }
        const jsonBody = JSON.stringify(body)
        executeRequest('http://localhost:8082/gift-certificate-rest/api/v1/gift-certificates', 'POST', jsonBody, {
            'Accept-language': 'en',
            'Content-Type': 'application/json'
        }, true)
            .then(data => {
                if (data.errorCode === 40004) {
                    setModalError(data.errorMessage.substring(data.errorMessage.indexOf(':') + 1))
                    props.setModalActiveAdd(true)
                } else {
                    setCounter(prevState => prevState + 1)
                    props.setPage(1)
                }
            })
        console.log('submitted adding certificate')
    }

    const handleSubmitAddTagClick = (name) => {
        const body = {
            "name": name
        }
        const jsonBody = JSON.stringify(body)
        executeRequest('http://localhost:8082/gift-certificate-rest/api/v1/tags', 'POST', jsonBody, {
            'Accept-language': 'en',
            'Content-Type': 'application/json'
        }, true)
            .then(data => {
                if (data.errorCode === 40901 || data.errorCode === 40004) {
                    setModalError(data.errorCode === 40004 ? data.errorMessage.substring(data.errorMessage.indexOf(':') + 1) : data.errorMessage)
                    setModalValue(value)
                    props.setModalActiveAdd(true)
                } else {
                    setCounter(prevState => prevState + 1)
                    props.setPage(1)
                }
            })
    }

    const handleSubmitEditClick = (value, name, desc, price, duration) => {
        const body = {
            "name": name,
            "description": desc,
            "price": price,
            "duration": duration,
            "createDate": value.createDate,
            "lastUpdateDate": value.lastUpdateDate,
            "tagSet": value.tagSet
        }
        const jsonBody = JSON.stringify(body)
        executeRequest(value.links.filter(elem => elem.rel === 'update')[0].href, 'PUT', jsonBody, {
            'Content-Type': 'application/json'
        }, true)
            .then(data => {
                if (data.errorCode === 40004) {
                    setModalError(data.errorMessage.substring(data.errorMessage.indexOf(':') + 1))
                    setModalValue(value)
                    setModalActiveEdit(true)
                } else {
                    setCounter(prevState => prevState + 1)
                    props.setPage(1)
                }
            })
    }

    const handleEditClick = (value) => {
        setModalValue(value)
        setModalActiveEdit(true)
    }

    const handleDeleteClick = (value) => {
        executeRequest(value.links.filter(elem => elem.rel === 'delete')[0].href, 'DELETE', null, {}, true)
            .then(() => {
                setCounter(prevState => prevState + 1)
                props.setPage(1)
            })
    }

    function draw(res) {
        const hookDict = {
            'orders': useOrderSearch(props.page),
            'users': useUserSearch(props.query, props.page),
            'tags': useTagSearch(props.query, props.page, counter),
            'certificates': useCertificateSearch(props.query, null, null, props.page, counter)
        }
        const headerDict = {
            'orders': (
                <tr className="table__header">
                    <th>ID</th>
                    <th>Final price</th>
                    <th>Purchase time</th>
                    <th>Certificates</th>
                    <th>Customer</th>
                </tr>
            ),
            'users': (
                <tr className="table__header">
                    <th>ID</th>
                    <th>Username</th>
                    <th>Creation Date</th>
                </tr>
            ),
            'tags': (
                <tr className="table__header">
                    <th>ID</th>
                    <th>Name</th>
                    <th>Creation Date</th>
                    <th>Actions</th>
                </tr>
            ),
            'certificates': (
                <tr className="table__header">
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Tags</th>
                    <th>Duration</th>
                    <th>Price</th>
                    <th>Creation Date</th>
                    <th>Last Modification Date</th>
                    <th>Actions</th>
                </tr>
            )
        }

        const compDict = {
            'orders': Order,
            'users': User,
            'tags': Tag,
            'certificates': Certificate
        }

        let hook = hookDict[res]

        const {
            content,
            hasMore,
            loading,
            empty
        } = hook

        const lastElem = getLastElemRef(loading, hasMore)

        const header = headerDict[res]
        const result = (
            <>
                {content.map((value, index) => {
                    let referral = content.length === index + 1 ? lastElem : null
                    return React.createElement(compDict[res], {
                        'value': value,
                        'referral': referral,
                        'handleEditClick': () => handleEditClick(value),
                        'handleDeleteClick': () => handleDeleteClick(value)
                    }, null)
                })}
                {
                    !loading && content.length === 0 &&
                    <section id="empty-result" className="content ">
                        <h1>Your search returned no results!</h1>
                    </section>
                }
            </>
        )

        return {header, result}
    }

    if (props.res === null) document.location.href = 'admin.html?res=certificates'
    const comp = draw(props.res)

    return (
        <>
            <table className="table">
                <thead>
                {comp.header}
                </thead>
                <tbody>
                {comp.result}
                </tbody>
            </table>
            <Modal
                active={activeEdit}
                setActive={setModalActiveEdit}
            >
                <ModalContentEdit
                    active={activeEdit}
                    setActive={setModalActiveEdit}
                    value={modalValue}
                    handleSubmit={handleSubmitEditClick}
                    modalError={modalError}
                    setModalError={setModalError}
                />
            </Modal>
            <Modal
                active={props.activeAdd}
                setActive={props.setModalActiveAdd}
            >
                {props.res === 'certificates' &&
                    <ModalContentAddCertificate
                        active={props.activeAdd}
                        setActive={props.setModalActiveAdd}
                        handleSubmit={handleSubmitAddCertificateClick}
                        modalError={modalError}
                        setModalError={setModalError}
                    />
                }
                {props.res === 'tags' &&
                    <ModalContentAddTag
                        active={props.activeAdd}
                        setActive={props.setModalActiveAdd}
                        handleSubmit={handleSubmitAddTagClick}
                        modalError={modalError}
                        setModalError={setModalError}
                    />
                }

            </Modal>
        </>
    )
}

function AdminPage(props) {
    // if (parseJwt(localStorage.getItem('accessToken')).roles.includes('ADMIN')) {
    //     document.location.href = '/admin.html'
    // } else {
    //     document.location.href = '/'
    // }
    const [activeAdd, setModalActiveAdd] = useState(false)
    const [query, setQuery] = useState('')
    const [page, setPage] = useState(1)

    const handleQueryChange = (e) => {
        setQuery(e.target.value)
        setPage(1)
    }

    const [username, setUsername] = useState('')
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
        localStorage.setItem('currentUser', null)
        setUsername('')
        document.location.href = 'login.html'
    }

    const handleAddClick = () => {
        setModalActiveAdd(true)
    }

    return (
        <>
            <header className="header">
                <div className="content header__content">
                    <div className="header__logo">
                        <a className="header__logo-link" href="/admin.html">
                            <img className="logo" src="assets/img/giftbox.png" alt="logo"/>Giftbox Admin UI
                        </a>
                    </div>

                    <div className="header__user-data">
                        <div className="header__username">
                            {'Hello, ' + username}
                        </div>
                        <div className="header__logout">
                            <button className="header__signout" onClick={handleSignoutClick}>Sign out</button>
                        </div>
                    </div>
                </div>
            </header>

            <div className="content">
                <div className="w3-sidebar w3-light-grey w3-bar-block content__list">
                    <h3 className="w3-bar-item">Resources</h3>
                    <a href="admin.html?res=certificates" className="w3-bar-item w3-button">Certificates</a>
                    <a href="admin.html?res=tags" className="w3-bar-item w3-button">Tags</a>
                    <a href="admin.html?res=users" className="w3-bar-item w3-button">Users</a>
                    <a href="admin.html?res=orders" className="w3-bar-item w3-button">Orders</a>
                </div>
                <div className="content__table">
                    <div className="table__container">
                        <input onChange={handleQueryChange} value={query} className="table__search" placeholder="Search..."/>
                        <button onClick={handleAddClick} className="table__add">Add item</button>
                    </div>
                    <Table
                        res={props.res}
                        query={query}
                        page={page}
                        setPage={setPage}
                        activeAdd={activeAdd}
                        setModalActiveAdd={setModalActiveAdd}
                    />
                </div>
            </div>
        </>
    )
}

const queryString = window.location.search
const urlParams = new URLSearchParams(queryString)
let res = urlParams.get('res')

const root = ReactDOM.createRoot(document.getElementById('root'))
root.render(<AdminPage
    res={res}
/>)