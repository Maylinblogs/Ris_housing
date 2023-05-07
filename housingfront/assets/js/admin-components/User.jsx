import React from "react";

export function User(props) {
    const user = props.value
    if (props.referral === null) {
        return (
            <tr>
                <td>{user.id}</td>
                <td>{user.username}</td>
                <td>{user.createDate}</td>
            </tr>
        )
    } else {
        return (
            <tr ref={props.referral}>
                <td>{user.id}</td>
                <td>{user.name}</td>
                <td>{user.createDate}</td>
            </tr>
        )
    }
}