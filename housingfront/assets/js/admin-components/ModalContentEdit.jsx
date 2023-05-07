import React, {useEffect, useState} from "react";

export function ModalContentEdit(props) {
    const [name, setName] = useState('')
    const [desc, setDesc] = useState('')
    const [price, setPrice] = useState('')
    const [duration, setDuration] = useState('')

    const handleCancel = () => {
        props.setActive(false)
        props.setModalError('')
    }

    useEffect(() => {
        setName(props.value.name)
        setDesc(props.value.description)
        setPrice(props.value.price)
        setDuration(props.value.duration)
    }, [props.active, props.value])

    return (
        <>
            <h2>Editing item {props.value.id}</h2>
            <h5 className="modal__error">{props.modalError}</h5>
            <input onChange={e => setName(e.target.value)} id="name" value={name} className="modal__input" type="text" placeholder="Name" />
            <input onChange={e => setDesc(e.target.value)} id="desc" value={desc} className="modal__input" type="text" placeholder="Description" />
            <input onChange={e => setPrice(e.target.value)} id="price" value={price} className="modal__input" type="text" placeholder="Price" />
            <input onChange={e => setDuration(e.target.value)} id="duration" value={duration} className="modal__input" type="text" placeholder="Duration" />
            <div className="modal__buttons">
                <button onClick={handleCancel} className="modal__button" type="button">Cancel</button>
                <button onClick={
                    () => {
                        props.setActive(false)
                        props.handleSubmit(props.value, name, desc, price, duration)
                    }
                } className="modal__button modal__confirm-button" type="button">Submit</button>
            </div>
        </>
    )
}