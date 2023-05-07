import React, {useCallback, useRef, useState} from "react";
import ReactDOM from "react-dom/client";
import useTagSearch from "./hooks/useTagSearch";
// import {BrowserRouter as Router, Route, Switch} from "react-router-dom";

function deepEqual(object1, object2) {
    const keys1 = Object.keys(object1);
    const keys2 = Object.keys(object2);
    if (keys1.length !== keys2.length) {
        return false;
    }
    for (const key of keys1) {
        const val1 = object1[key];
        const val2 = object2[key];
        const areObjects = isObject(val1) && isObject(val2);
        if (
            areObjects && !deepEqual(val1, val2) ||
            !areObjects && val1 !== val2
        ) {
            return false;
        }
    }
    return true;
}
function isObject(object) {
    return object != null && typeof object === 'object';
}

function ListElement(props) {
    let clazz = 'dropdown__tag'
    const referral = props.referral
    if (props.isSelected) {
        clazz += ' selected'
    }
    if (referral != null) {
        return (
            <button ref={referral} type="button" className={clazz} onClick={props.onClick}>{props.tag.name}</button>
        )
    } else {
        return (
            <button type="button" className={clazz} onClick={props.onClick}>{props.tag.name}</button>
        )
    }
}

function SearchBar(props) {
    const [selected, setSelected] = useState([])
    const [page, setPage] = useState(1)
    const [name, setName] = useState('')
    const {
        content,
        loading,
        empty,
        hasMore
    } = useTagSearch(name, page, 0)

    const observer = useRef()
    const lastElem = useCallback(node => {
        if (loading) return
        if (observer.current) observer.current.disconnect()
        observer.current = new IntersectionObserver(entries => {
            if (entries[0].isIntersecting && hasMore) {
                setPage(prevPageNumber => prevPageNumber + 1)
            }
        })
        if (node) observer.current.observe(node)
    }, [loading, hasMore])

    const handleFormSubmit = (e) => {
        e.preventDefault()
        const name = document.getElementById('search-bar').value
        const tags = selected.map(value => value.name).join(', ')
        document.location.href = `/?searchName=${name}&searchTags=${tags}`
    }

    const handleDropbutton = () => {
        document.getElementById('admin-components-list').classList.toggle('show')
    }

    const handleTagSearch = (e) => {
        setName(e.target.value)
        setPage(1)
    }

    const handleClick = (value) => {
        if (selected.filter((elem) => deepEqual(elem, value)).length > 0) {
            setSelected(prevState => {
                return prevState.filter(elem => !deepEqual(value, elem))
            })
        } else {
            setSelected(prevState => {
                let newState = prevState.slice(0, prevState.length)
                newState.push(value)
                return newState
            })
        }
    }

    return (
        <form onSubmit={handleFormSubmit} id="search-form">
            <input id="search-bar" className="header__search-bar" type="text" placeholder="Поиск..." name="search"/>
            <div id="tag-list" className="search__tag-dropdown">
                <input value={name} onChange={handleTagSearch} type="text" placeholder="Search tags..." id="tag-search-bar"/>
                {selected.map((value, index) => {
                    let referral = content.length === index + 1 ? lastElem : null
                    return (
                        <ListElement
                            isSelected={selected.filter((elem) => deepEqual(elem, value)).length > 0}
                            tag={value}
                            onClick={() => handleClick(value)}
                            referral={referral}
                            // key={value.id}
                        />
                    )
                })
                }
                {content.filter((elemOuter) => !selected.filter((elem) => deepEqual(elem, elemOuter)).length > 0)
                    .map((value, index) => {
                        let referral = content.length === index + 1 ? lastElem : null
                        return (
                            <ListElement
                                isSelected={selected.filter((elem) => deepEqual(elem, value)).length > 0}
                                tag={value}
                                onClick={() => handleClick(value)}
                                referral={referral}
                                // key={value.id}
                            />
                        )
                    })}
            </div>
            <button className="header__search-button" type="submit"><span className="material-icons glass">search</span>
            </button>
        </form>
    );
}

const root = ReactDOM.createRoot(document.getElementById("search-container"))
root.render(<SearchBar/>)