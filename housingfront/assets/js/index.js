let catalog = document.getElementById('catalog')
let catalogContainer = document.getElementById('catalog-container')
let loader = document.getElementById('content-loader')
let sortBy = 'price'
let sortType = 'desc'
let currPage = 1
let keepLoading = true
let searchForm = document.getElementById('search-form')
let scrollUp = document.getElementById('scroll-up')
let scrollDown = document.getElementById('scroll-down')
let showScrollToTop = true
let lastScrollPos = 0

scrollUp.addEventListener('click', () => {
    showScrollToTop = false
    lastScrollPos = catalog.scrollTop
    catalog.scrollTop = 0
    scrollDown.style.display = 'flex'
    scrollUp.style.display = 'none'
})

scrollDown.addEventListener('click', () => {
    showScrollToTop = true
    catalog.scrollTop = lastScrollPos
    scrollUp.style.display = 'flex'
    scrollDown.style.display = 'none'
})

searchForm.addEventListener('submit', () => {
    let query = document.getElementById('search-bar').value
    document.location.href = `/search.html?searchName=${query}`
})

const loadPage = (page, size) => {
    localStorage.setItem('currentPage', page)
    let value = select.value;
    console.log(value)
    fetch(`http://localhost:8082/gift-certificate-rest/api/v1/gift-certificates?page=${page}&size=${size}&sortBy=${sortBy}&sortType=${sortType}`)
        .then( (response) => {
            return response.json()
        })
        .catch( (error) => {
            console.log(error)
            // connectionError()
        })
        .then((data) => {
            if (data) {
                localStorage.setItem('data', JSON.stringify(data))
                createPage(data)
            }
        })
}

const createPage = (data) => {
    if (data.errorCode === 40005) {
        keepLoading = false
        hideLoader()
    } else {
        data.forEach(certificate => {
            // let card = document.createElement('div')
            // card.classList.add('card')
            // let img = document.createElement('img')
            // img.setAttribute('src', 'assets/img/jeans3.jpg')
            // img.style.width = '100%'
            // card.appendChild(img)
            // let name = document.createElement('h1')
            // let a = document.createElement('a')
            // a.setAttribute('href', `product.html?id=${certificate.id}`)
            // a.innerText = certificate.name
            // a.classList.add('name')
            // name.appendChild(a)
            // card.appendChild(name)
            // let price = document.createElement('p')
            // price.classList.add('price')
            // price.innerText = '$' + certificate.price
            // card.appendChild(price)
            // let button = document.createElement('button')
            // let cart
            // cart = localStorage.getItem('cart')
            // if (cart?.includes(certificate.id)) {
            //     button.innerText = 'Remove From Cart'
            //     button.addEventListener('click', () => {
            //         console.log(cart)
            //         cart = cart.replace(certificate.id, '')
            //         localStorage.setItem('cart', cart)
            //         location.reload()
            //     })
            //     button.classList += 'card__added'
            // } else {
            //     button.innerText = 'Add to Cart'
            //     button.addEventListener('click', () => {
            //         cart = cart == null ? certificate.id : cart + ' ' + certificate.id
            //         localStorage.setItem('cart', cart)
            //         location.reload()
            //     })
            //     button.classList += 'card__add-to-cart'
            // }
            // card.appendChild(document.createElement('p').appendChild(button))
            // catalogContainer.appendChild(card)
            //
        });
    }
}

const showLoader = () => {
    loader.style.display = 'flex'
}

const hideLoader = () => {
    loader.style.display = 'none'
}

let throttleTimer;
 
const throttle = (callback, time) => {
  if (throttleTimer) return
 
  throttleTimer = true
 
  setTimeout(() => {
    callback()
    throttleTimer = false
  }, time)
}

const handleScroll = () => { 
    if ((catalog.scrollTop > 20) && showScrollToTop) {
        scrollUp.style.display = 'flex'
    } else {
        scrollUp.style.display = 'none'
    }

    if (keepLoading) {
        throttle(() => {
            if (catalog.scrollTop + catalog.clientHeight >= catalog.scrollHeight) {
                loadPage(++currPage, 15)
            }
        }, 1000)
    }
}

catalog.addEventListener('scroll', handleScroll)

window.addEventListener('load', () => {
    loadPage(currPage, 15)
})

// searchForm.addEventListener('submit', () => {
//     fetch(`http://localhost:8082/gift-certificate-rest/api/v1/gift-certificates?page=${page}&size=${size}&sortBy=${sortBy}&sortType=${sortType}`)
//         .then( (response) => {
//             return response.json()
//         })
//         .catch( (error) => {
//             console.log(error)
//             // connectionError()
//         })
//         .then((data) => {
//             if (data) {
//                 localStorage.setItem('data', JSON.stringify(data))
//                 createPage(data)
//             }
//         })
// })

// HEADER TAG SEARCH----------------------------------------------

const handleDropbutton = () => {
    document.getElementById('admin-components-list').classList.toggle('show');
}

const filterTags = () => {
    console.log('filtering')
}