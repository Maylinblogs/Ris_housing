import React, { useState, useRef, useCallback, useEffect } from 'react';
import ReactDOM from 'react-dom/client';
import useCertificateSearch from "./hooks/useCertificateSearch";
import executeRequest from "../js/sender"
import {host} from "./host-const"

class Card extends React.Component {
    handleClick() {
        let cart
        cart = localStorage.getItem('cart')

        if (cart?.includes(this.props.certificate.id)) {
            cart = cart.replace(this.props.certificate.id, '')
            cart = cart.replace(/\s+/g,' ').trim();
            localStorage.setItem('cart', cart)
        } else {
            cart = cart == null ? this.props.certificate.id : cart + ' ' + this.props.certificate.id
            localStorage.setItem('cart', cart)
        }
        this.forceUpdate()
    }

    renderButton(id) {

        return (
            <a
                className={'card__add-to-cart'}
                href={"/product.html?id=" + this.props.certificate.id}
            >Забронировать</a>
        )
    }

    render() {
        return (
            <div className={"card"}>
                <img src="assets/img/house.jpg" alt="house" style={{width: 100 + '%'}}/>
                {this.props.user && <button onClick={() => this.props.toggleFavs(this.props.certificate.id)} className={`fav-btn ${this.props.favs.includes(this.props.certificate.id) ? "active" : ""}`}>{this.props.favs.includes(this.props.certificate.id) ? <span>&#x2764;&#xfe0f;</span> : <span>&#x2661;</span>}</button>}
                <div className='card-info'>
                    <div>Город: {this.props.certificate.city}</div>
                    <div>Адрес: {this.props.certificate.streetName}, {this.props.certificate.houseNumber}</div>
                    <div>{this.props.certificate.floorNumber} этаж, {this.props.certificate.square} кв.м.</div>
                    <p className="price">${this.props.certificate.price}</p>
                </div>
                {this.renderButton(this.props.certificate.id)}
            </div>
        );
    }
}

function CatalogPage(props) {
    const [page, setPage] = useState(0)
    const [favs, setFavs] = useState([])
    const [params, setParams] = useState({
        sortBy: 'price',
        sortType: 'asc'
    })
    const [content, setContent] = useState([])
    const [endOfPage, setEndOfPage] = useState(false)
    const [loading, setLoading] = useState(true)
    const [hasMore, setHasMore] = useState(true)
    const [needLoad, setNeedLoad] = useState(true)
    let throttleTimer;
    
    useEffect(() => {
        console.log(hasMore, needLoad);
        if (hasMore && needLoad) {
            setLoading(true)
            setNeedLoad(false)
            let url = `http://${host}/housing/estate?sort_by=${params.sortBy}&sort_dir=${params.sortType}&page=${page}&size=10`
            if (searchName != null) url += `&city=${searchName}`
            executeRequest(url, 'GET', null, {}, false)
                .catch((error) => {
                    console.log(error)
                })
                .then((data) => {
                    if (data.length === 0) {
                        setLoading(false)
                        setHasMore(false)
                    } else {
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

                        }
                    }
                })
            setPage(prev => prev + 1)
        }
    }, [needLoad, hasMore])

    useEffect(() => {
        endOfPage && setNeedLoad(true)
        setEndOfPage(false)
    }, [endOfPage])

    const userId = JSON.parse(localStorage.getItem('userID'))

    const toggleFavs = useCallback(id => {
        if (favs.includes(id)) {
            let url = `http://${host}/housing/user/${userId}/favourites`
            executeRequest(url, 'DELETE', JSON.stringify({ estateId: id }), {
                'Content-Type': 'application/json',
                'Accept-language': 'en'
            }, false)
                .catch((error) => {
                    console.log(error)
                })
                .then((data) => {
                    setFavs(prev => prev.filter(e => e !== id))
                })
        } else {
            let url = `http://${host}/housing/user/${userId}/favourites`
            executeRequest(url, 'POST', JSON.stringify({ estateId: id }), {
                'Content-Type': 'application/json',
                'Accept-language': 'en'
            }, false)
                .catch((error) => {
                    console.log(error)
                })
                .then((data) => {
                    setFavs(prev => [...prev, id])
                })
        }
    }, [favs])

    useEffect(() => {
        document.getElementById('sorting-type-selector').addEventListener('change', (e) => {
            setPage(0)
            setContent([])
            setHasMore(true)
            setNeedLoad(true)
            switch (e.target.value) {
                case 'expensive':
                    setParams({
                        sortBy: 'price',
                        sortType: 'desc'
                    })
                    break
                case 'large':
                    setParams({
                        sortBy: 'square',
                        sortType: 'desc'
                    })
                    break
                case 'cheap':
                    setParams({
                        sortBy: 'price',
                        sortType: 'asc'
                    })
                    break
                case 'small':
                    setParams({
                        sortBy: 'square',
                        sortType: 'asc'
                    })
                    break
            }
        });
    }, [])

    useEffect(() => {
        const userId = JSON.parse(localStorage.getItem('userID'))
        let url = `http://${host}/housing/user/${userId}/favourites`
        executeRequest(url, 'GET', null, {}, false)
            .catch((error) => {
                console.log(error)
            })
            .then((data) => {
                setFavs(data.map(e => e.id))
            })
    }, [])

    const handleScroll = () => {
        if ((catalog.scrollTop > 20) && showScrollToTop) {
            scrollUp.style.display = 'flex'
        } else {
            scrollUp.style.display = 'none'
        }
       
        if (catalog.scrollHeight - (catalog.scrollTop + catalog.clientHeight) < 100) {
            setEndOfPage(true)
        }
    }

    useEffect(() => {
        catalog.addEventListener('scroll', handleScroll)
        return () => catalog.removeEventListener('scrolll', handleScroll)
    }, [])

    const renderCard = (certificate, index) => {
        return (
            <Card
                certificate={certificate}
                key={certificate.id}
                favs={favs}
                toggleFavs={toggleFavs}
                user={userId}
            />
        )
    }

    return (
        <>
            {content.map((value, index) => {
                return renderCard(value, index)
            })}
            {
                loading &&
                <section id="content-loader" className="content content__loader">
                    <div className="content__spinner"></div>
                </section>
            }
            {
                !hasMore &&
                <section id="empty-result" className="content ">
                    <h1>Больше ничего не найдено!</h1>
                </section>
            }
        </>
    )
}

let scrollUp = document.getElementById('scroll-up')
let scrollDown = document.getElementById('scroll-down')
let showScrollToTop = true
let lastScrollPos = 0
const queryString = window.location.search
const urlParams = new URLSearchParams(queryString)
let searchName = urlParams.get('searchName')
let searchDescription = urlParams.get('searchDescription')
let searchTags = urlParams.get('searchTags')

scrollUp.addEventListener('click', () => {
    showScrollToTop = false
    lastScrollPos = catalog.scrollTop
    catalog.scrollTop = 0
    scrollDown.style.display = 'flex'
    scrollUp.style.display = 'none'
})

scrollDown.addEventListener('click', () => {
    showScrollToTop = true
    catalog.scrollTop = lastScrollPos
    scrollUp.style.display = 'flex'
    scrollDown.style.display = 'none'
})

const root = ReactDOM.createRoot(document.getElementById("catalog-container"))
root.render(<CatalogPage
    searchName={searchName}
    searchDescription={searchDescription}
    searchTags={searchTags}
/>)