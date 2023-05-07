import ReactDOM from "react-dom/client";
import React, {useState} from "react";
import executeRequest from "./sender";
import useCartCreation from "./hooks/useCartCreation";

function Card({info}) {

    return (
        <div className="card">
            <div className="card__left">
                <img src="assets/img/house.jpg" alt="thumbnail" className="creds__thumbnail" />
                <div>
                    <div className="card__city">{info.estate.city}</div>
                    <div>{info.estate.streetName}, {info.estate.houseNumber}</div>
                    <br/>
                    <div>Дата заезда: {new Date(Date.parse(info.arriving)).toLocaleString("ru-RU", {day: "numeric", month: "long", year: "numeric"})}</div>
                    <div>{info.days} дней, {info.peopleCount} человек(а)</div>
                </div>
            </div>
            <div className="price">${info.price}</div>
        </div>
    )
}

function CartPage(props) {
    const cartFromStorage = props.user
    const {
        certificates
    } = useCartCreation(cartFromStorage)

    console.log(certificates);

    return (
        <>
            {certificates.length === 0 &&
                <div className="empty-cart">У вас еще нет заказов!</div>
            }
            {certificates.length !== 0 &&
                <div class="cart__card-container" >
                    {certificates.map(certificate => <Card info={certificate} />)}
                </div>
            }
        </>
    )
}

const root = ReactDOM.createRoot(document.getElementById("cart"))
root.render(<CartPage
    user={JSON.parse(localStorage.getItem('userID'))}
/>)