import React from "react";

export function Certificate(props) {
    const certificate = props.value
    if (props.referral === null) {
        return (
            <tr>
                <td>{certificate.id}</td>
                <td>{certificate.name}</td>
                <td>{certificate.description}</td>
                <td>{certificate.tagSet.map((value) => value.name + ' | ')}</td>
                <td>{certificate.duration} day(s)</td>
                <td>{certificate.price}$</td>
                <td>{certificate.createDate}</td>
                <td>{certificate.lastUpdateDate}</td>
                <td><button onClick={props.handleEditClick} className="action__edit">Edit</button><button onClick={props.handleDeleteClick} className="action__delete">Delete</button></td>
            </tr>
        )
    } else {
        return (
            <tr ref={props.referral}>
                <td>{certificate.id}</td>
                <td>{certificate.name}</td>
                <td>{certificate.description}</td>
                <td>{certificate.tagSet.map((value) => value.name + ' | ')}</td>
                <td>{certificate.duration} day(s)</td>
                <td>{certificate.price}$</td>
                <td>{certificate.createDate}</td>
                <td>{certificate.lastUpdateDate}</td>
                <td>Edit Delete</td>
            </tr>
        )
    }

}