import React from "react";

export function Order(props) {
    const order = props.value
    if (props.referral === null) {
        return (
            <tr>
                <td>{order.orderId}</td>
                <td>{order.finalPrice}$</td>
                <td>{order.purchaseTime}</td>
                <td>{order.certificates?.map((value) => value.id + ' | ')}</td>
                <td>{order.customer?.id}</td>
            </tr>
        )
    } else {
        return (
            <tr ref={props.referral}>
                <td>{order.orderId}</td>
                <td>{order.finalPrice}$</td>
                <td>{order.purchaseTime}</td>
                <td>{order.certificates?.map((value) => value.id + ' | ')}</td>
                <td>{order.customer?.id}</td>
            </tr>
        )
    }
}