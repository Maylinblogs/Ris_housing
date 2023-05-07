import React, { useState, useRef, useCallback, useEffect } from 'react';
import ReactDOM from 'react-dom/client';
import useCertificateSearch from "./hooks/useCertificateSearch";
import executeRequest from "../js/sender"
import { host } from "./host-const"

class Card extends React.Component {
    handleClick() {
        let cart
        cart = localStorage.getItem('cart')

        if (cart?.includes(this.props.certificate.id)) {
            cart = cart.replace(this.props.certificate.id, '')
            cart = cart.replace(/\s+/g, ' ').trim();
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
                <img src="assets/img/house.jpg" alt="house" style={{ width: 100 + '%' }} />
                <button onClick={() => this.props.toggleFavs(this.props.certificate.id)} className={`fav-btn active`}><span>&#x2764;&#xfe0f;</span></button>
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
    const [favs, setFavs] = useState([])


    const toggleFavs = useCallback(id => {
        const userId = JSON.parse(localStorage.getItem('userID'))
        let url = `http://${host}/housing/user/${userId}/favourites`
        executeRequest(url, 'DELETE', JSON.stringify({ estateId: id }), {
            'Content-Type': 'application/json',
            'Accept-language': 'en'
        }, false)
            .catch((error) => {
                console.log(error)
            })
            .then((data) => {
                setFavs(prev => prev.filter(e => e.id !== id))
            })
    }, [favs])

    useEffect(() => {
        const userId = JSON.parse(localStorage.getItem('userID'))
        let url = `http://${host}/housing/user/${userId}/favourites`
        executeRequest(url, 'GET', null, {}, false)
            .catch((error) => {
                console.log(error)
            })
            .then((data) => {
                setFavs(data)
            })
    }, [])

    const renderCard = (certificate, index) => {
        return (
            <Card
                certificate={certificate}
                key={certificate.id}
                toggleFavs={toggleFavs}
            />
        )
    }

    return (
        <>
            {favs.length === 0 &&
                <section className="content">
                    <div className="empty-cart">У вас нет избранных товаров</div>
                </section>
            }
            {favs.length !== 0 &&
                <>
                    <section className="content">
                        <div className="favs-header">Избранное</div>
                    </section>
                    {favs.map((value, index) => {
                        return renderCard(value, index)
                    })}
                </>
            }
        </>
    )
}

const queryString = window.location.search
const urlParams = new URLSearchParams(queryString)
let searchName = urlParams.get('searchName')
let searchDescription = urlParams.get('searchDescription')
let searchTags = urlParams.get('searchTags')


const root = ReactDOM.createRoot(document.getElementById("favs"))
root.render(<CatalogPage
    searchName={searchName}
    searchDescription={searchDescription}
    searchTags={searchTags}
/>)