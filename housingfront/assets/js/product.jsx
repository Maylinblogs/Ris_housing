import ReactDOM from "react-dom/client";
import React, {useCallback, useEffect, useState} from "react";
import executeRequest from "./sender";
import { host } from "./host-const"

class Button extends React.Component {
    render() {
        return (
            <button disabled={this.props.disabled} className="card__button" onClick={() => this.props.onClick()}>Оформить</button>
        )
    }
}

function Product(props) {
    const [certificate, setCertificate] = useState({})
    const [avg, setAvg] = useState("")
    const [loading, setLoading] = useState(true)
    const [fetching, setFetching] = useState(false)
    const [peopleCount, setPeopleCount] = useState(1)
    const [days, setDays] = useState(1)
    const [date, setDate] = useState("")
    const [price, setPrice] = useState(0)
    let productPresent = (props.id !== null)
    const user = JSON.parse(localStorage.getItem('userID'))

    useEffect( () => {
        const fetchData = async () => {
            const res = await executeRequest(`http://${host}/housing/estate/${id}`, 'GET', null, {}, false)
                .then(data => {
                    productPresent = data.errorCode === 40003 || data.errorCode === 40401
                    return data
                })
            setCertificate(res)
            console.log(res);
        }
        fetchData()
        executeRequest(`http://${host}/housing/review/avg/${id}`, 'GET', null, {}, false)
            .catch((error) => {
                console.log(error)
            })
            .then((data) => {
                setAvg(data)
            })
    }, [])

    const getPrice = () => {
        setLoading(true)
        const body = {
            peopleCount,
            days,
            arriving: date,
            estateId: id
        }
        executeRequest(`http://${host}/housing/deal/calculate-price`, 'POST', JSON.stringify(body), {
            'Content-Type': 'application/json',
            'Accept-language': 'en'
        }, false)
            .catch((error) => {
                console.log(error)
            })
            .then((data) => {
                setPrice(data)
                setLoading(false)
            })
    }

    const createDeal = () => {
        setFetching(true)
        if (!loading) {
            const body = {
                peopleCount,
                days,
                arriving: date,
                estateId: id,
                price,
                userId: user
            }
            executeRequest(`http://${host}/housing/deal`, 'POST', JSON.stringify(body), {
                'Content-Type': 'application/json',
                'Accept-language': 'en'
            }, false)
                .catch((error) => {
                    console.log(error)
                })
                .then((data) => {
                    window.location.href = "/"
                })
        }
    }

    useEffect(() => {
        date && peopleCount && days && getPrice()
    }, [date, peopleCount, days])

    if (productPresent) {
        let shortDesc = certificate.description
        const admin = JSON.parse(localStorage.getItem("admin"))
        shortDesc = shortDesc?.length > 260 ? shortDesc.substring(0, 260).substring(0, shortDesc.lastIndexOf(' ')) + '...' : shortDesc
        return (
            <>
                <div className="product__top-row">
                    <div className="product__thumbnail">
                        <div style={{ position: "relative", overflow: "hidden" }}><a href="https://yandex.by/maps/157/minsk/?utm_medium=mapframe&utm_source=maps" style={{ color: "#eee", fontSize: "12px", position: "absolute", top: "0px" }}>Минск</a><a href={`https://yandex.by/maps/157/minsk/?ll=${certificate.longitude}%2C${certificate.latitude}&utm_medium=mapframe&utm_source=maps&z=15.75`} style={{ color: "#eee", fontSize: "12px", position: "absolute", top: "14px" }}>Яндекс Карты</a><iframe src={`https://yandex.by/map-widget/v1/?ll=${certificate.longitude}%2C${certificate.latitude}&z=15.75`} width="560" height="400" frameborder="1" allowfullscreen="true" style={{ position: "relative" }}></iframe></div>
                        {admin && <a href={`http://${host}/housing/estate/${certificate.id}/report`} download className="card__download">Скачать отчет</a>}
                    </div>
                    <div className="product__interactive-block">
                        <div className="product__info">
                            <h3>Информация</h3>
                            <div>Город: {certificate.city}</div>
                            <div>Адрес: {certificate.streetName}, {certificate.houseNumber}</div>
                            <div>{certificate.floorNumber} этаж, {certificate.square} кв.м.</div>
                            {avg && <div>Рейтинг: {avg.toFixed(1)} ({avg <= 4 ? <spnan className="bad">Ужасно</spnan> : avg > 4 && avg <= 7 ? <spnan className="mid">Средне</spnan> : <spnan className="perf">Отлично</spnan>})</div>}
                            <div>Цена: ${certificate.price}</div>
                        </div>
                        {user &&
                            <>
                                <br/>
                                <div className="product__info">
                                    <h3>Параметры брони</h3>
                                    <div className="product__inputs">
                                        <label className="product__label">
                                            Количество челвоек
                                            <input value={peopleCount} onChange={e => { setPeopleCount(e.target.value) }} type="number" min={1} max={5} placeholder="Количество человек"></input>
                                        </label>
                                        <label className="product__label">
                                            Количество дней
                                            <input value={days} onChange={e => { setDays(e.target.value) }} type="number" min={1} max={10} placeholder="Количество дней"></input>
                                        </label>
                                        <label className="product__label">
                                            Дата заезда
                                            <input value={date} onChange={e => { setDate(e.target.value) }} type="date" min={new Date().toISOString().substr(0, 10)} placeholder="Дата заезда"></input>
                                        </label>
                                        <div>Итоговая цена: {loading || !price ? "..." : "$" + price.toFixed(2)}</div>
                                    </div>
                                </div>
                                <Button
                                    certificate={certificate}
                                    onClick={createDeal}
                                    disabled={fetching || loading}
                                />
                            </>
                        }
                        
                    </div>
                </div>
            </>
        )
    }
}

const Reviews = ({id}) => {
    const [rev, setRev] = useState([])
    const [note, setNote] = useState(1)
    const [text, setText] = useState("")
    const user = JSON.parse(localStorage.getItem('userID'))

    useEffect(() => {
        let url = `http://${host}/housing/review/estate/${id}`
        executeRequest(url, 'GET', null, {}, false)
            .catch((error) => {
                console.log(error)
            })
            .then((data) => {
                setRev(data)
                console.log(data);
            })
    }, [])

    const sendReview = useCallback(() => {
        let url = `http://${host}/housing/review`
        const body = {
            grade: note,
            text: text,
            authorId: user,
            estateId: id
        }
        executeRequest(url, 'POST', JSON.stringify(body), {
            'Content-Type': 'application/json',
            'Accept-language': 'en'
        }, false)
            .catch((error) => {
                console.log(error)
            })
            .then((data) => {
                setRev(prev => [...prev, data])
                setNote(1)
                setText("")
            })
    }, [note, text])

    return(
        <div className="revs">
            {user && 
                <div className="rev__card">
                    <div className="rev__card-header">Оставить отзыв</div>
                    <textarea value={text} onChange={e => setText(e.target.value)} className="rev__textarea" rows={5} placeholder="Введите отзыв"></textarea>
                    <div>
                        <div>Ваша оценка: {note}</div>
                        <input value={note} onChange={e => setNote(Number(e.target.value))} className="rev__range" type="range" min={1} max={10} step={1}></input>
                        <div className="rev__notes">
                            <div className={note == 1 ? "active bad" : ""}>1</div>
                            <div className={note == 2 ? "active bad" : ""}>2</div>
                            <div className={note == 3 ? "active bad" : ""}>3</div>
                            <div className={note == 4 ? "active bad" : ""}>4</div>
                            <div className={note == 5 ? "active mid" : ""}>5</div>
                            <div className={note == 6 ? "active mid" : ""}>6</div>
                            <div className={note == 7 ? "active mid" : ""}>7</div>
                            <div className={note == 8 ? "active perf" : ""}>8</div>
                            <div className={note == 9 ? "active perf" : ""}>9</div>
                            <div className={note == 10 ? "active perf" : ""}>10</div>
                        </div>
                    </div>
                    <button className="card__button" onClick={sendReview}>Оставить отзыв</button>
                </div>
            }
            {rev.map(r => 
                <div className="rev__card">
                    <div className="rev__author">{r.author.firstName}</div>
                    <div>Оценка: {r.grade} ({r.grade <= 4 ? <spnan className="bad">Ужасно</spnan> : r.grade > 4 && r.grade <= 7 ? <spnan className="mid">Средне</spnan> : <spnan className="perf">Отлично</spnan>})</div>
                    <hr/>
                    <div className="rev__text">{r.text}</div>
                </div>
            )}
        </div>
    )
}

const queryString = window.location.search
const urlParams = new URLSearchParams(queryString)
let id = urlParams.get('id')

const root = ReactDOM.createRoot(document.getElementById('product'))
root.render(<Product
    id={id}
/>)

const root1 = ReactDOM.createRoot(document.getElementById('revs'))
root1.render(<Reviews
    id={id}
/>)