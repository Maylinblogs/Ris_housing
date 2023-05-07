import React from "react";
import executeRequest from "../sender";

export function Tag(props) {
    const tag = props.value
    if (props.referral === null) {
        return (
            <tr>
                <td>{tag.id}</td>
                <td>{tag.name}</td>
                <td>{tag.createDate}</td>
                <td><button onClick={props.handleDeleteClick} className="action__delete">Delete</button></td>
            </tr>
        )
    } else {
        return (
            <tr ref={props.referral}>
                <td>{tag.id}</td>
                <td>{tag.name}</td>
                <td>{tag.createDate}</td>
                <td><button onClick={props.handleDeleteClick} className="action__delete">Delete</button></td>
            </tr>
        )
    }
}