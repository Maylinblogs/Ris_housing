import React, {useEffect, useState} from "react";

export function ModalContentAddTag(props) {
    const [name, setName] = useState('')

    const handleCancel = () => {
        props.setActive(false)
        props.setModalError('')
    }

    useEffect(() => {
        setName('')
    }, [props.active])

    return (
        <>
            <h2>Adding certificate</h2>
            <h5 className="modal__error">{props.modalError}</h5>
            <input onChange={e => setName(e.target.value)} id="name" value={name} className="modal__input" type="text" placeholder="Name" />
            <div className="modal__buttons">
                <button onClick={handleCancel} className="modal__button" type="button">Cancel</button>
                <button onClick={
                    () => {
                        props.setActive(false)
                        props.handleSubmit(name)
                    }
                } className="modal__button modal__confirm-button" type="button">Submit</button>
            </div>
        </>
    )
}