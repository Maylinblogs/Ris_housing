export default function executeRequest(url, method, body, headers, authorizeRequest) {
    if (authorizeRequest) {
        const accessToken = localStorage.getItem('accessToken')
        headers['Authorization'] = 'Bearer ' + accessToken
    }
    const options = {
        method: method,
        headers: headers,
        body: body
    }

    return fetch(url, options)
        .then(response => {
            if (response.status !== 204) return response.json()
        })
        .then(data => {
            if (data) {
                switch (data.errorCode) {
                    case 40101:
                        console.log('refresh')
                        if (refreshAccessToken()) {
                            console.log('refresh successful')
                            return executeRequest(url, method, body, headers, true)
                        } else {
                            console.log('refresh unsuccessful')
                            document.location.href = '/login.html'
                        }
                        break
                    case 40102:
                        console.log('invalid token')
                        document.location.href = '/login.html'
                        break
                    default:
                        if (data.status === 403) {
                            console.log('forbidden')
                        } else {
                            return data
                        }
                }
            }
        });
}

async function refreshAccessToken() {
    const refreshToken = localStorage.getItem('refreshToken')
    const options = {
        method: 'GET',
        headers: {
            'Accept-language': 'en',
            'Authorization': 'Bearer ' + refreshToken
        }
    }
    return await fetch('http://localhost:8082/gift-certificate-rest/api/v1/users/refresh-token', options)
        .then(response => response.json())
        .then(data => {
            if (data.errorCode === 40101 && data.errorCode === 40102) {
                return false
            } else {
                localStorage.setItem('accessToken', data.accessToken)
                localStorage.setItem('refreshToken', data.refreshToken)
                return true
            }
        })
}