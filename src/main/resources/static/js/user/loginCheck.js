const notLoginNav =
    `
<div class="container">
<div
    class="d-flex flex-wrap align-items-center justify-content-center">
    <!-- 首頁logo -->
    <a href="/user/" class="d-block"> <img
        src="/imgs/user/checKInOut_logo.png" alt="checkinout"
        width="72" height="72">
    </a>
    <ul class="nav col justify-content-end login">
        <li><a href="/user/order" class="nav-link px-2 link-body-emphasis">訂單查詢</a></li>
        <li><a href="/user/cart" class="nav-link px-2 link-body-emphasis">購物車</a></li>
        <li><a href="" class="d-block link-body-emphasis text-decoration-none px-2 py-2" id="navLoginBtn">登入 / 註冊</a></li>
    </ul>
`

const loginNav = `
    <div class="container">
        <div class="d-flex flex-wrap align-items-center justify-content-center">
            <!-- 首頁logo -->
            <a href="/user/" class="d-block"> <img src="/imgs/user/checKInOut_logo.png" alt="checkinout"
                    width="72" height="72">
            </a>
            <!-- 登入後 -->
            <ul class="nav col justify-content-end login">
                <li><a href="/user/order" class="nav-link px-2 link-body-emphasis">訂單查詢</a></li>
                <li><a href="/user/cart" class="nav-link px-10 link-body-emphasis">購物車</a></li>
                <li>
                    <div class="dropdown text-end px-2 py-20">
                        <a href="#" class="d-block link-body-emphasis text-decoration-none dropdown-toggle"
                            data-bs-toggle="dropdown" aria-expanded="false"> <img src=""
                                alt="mdo" width="32" height="32" class="rounded-circle">
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end text-center mt-3 border-0 shadow-sm"
                            data-bs-display="static">
                            <li><a class="dropdown-item" href="/user/profile">會員中心</a></li>
                            <li><a class="dropdown-item" href="/user/order">訂單管理</a></li>
                            <li><a class="dropdown-item" href="/user/favorite">我的最愛</a></li>
                            <li><a class="dropdown-item" href="/user/coupon">優惠券</a></li>
                            <li>
                                <hr class="dropdown-divider">
                            </li>
                            <li><a class="dropdown-item" href="/user/contactUs">客服中心</a></li>
                            <li><a class="dropdown-item" href="#" id="logout">登出</a></li>
                        </ul>
                    </div>
                </li>
            </ul>
        </div>
    </div>
`;
const loginModalDiv = `
	<div class="modal fade" id="loginModal" tabindex="-1" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered" style="max-width:25vw;">
			<div class="modal-content text-center">
				<div class="modal-body" id="login-modal-body">
				</div>
			</div>
		</div>
	</div>
`

let loginMessage = "";

const loginMessageDiv =`
<div id="loginMessage" class="fs-5 mb-3"></div>
<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">確定</button>
`

const loginFormView =`
    <form class="px-4 py-3" id="login_list" action="Post">
        <div class="mb-3">
            <label for="account" class="form-label">Email/帳號</label>
            <input type="email" class="form-control" id="account" name="account" placeholder="請輸入帳號" required>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">密碼</label>
            <input type="password" class="form-control" id="password" name="password" placeholder="請輸入密碼" autocomplete="off" required>
        </div>
        <div class="d-grid gap-2">
            <button class="btn btn-primary login" type="submit">登入</button>
            <button class="btn btn-primary" type="button">Sign in with Google</button>
            <button class="btn btn-primary" id="register" type="button">註冊</button>
        </div>
    </form>
`;


document.addEventListener('DOMContentLoaded', function () {
    showLoginView();
});

// 通用 API 請求函數
function apiRequest(url, method = 'GET', data = null) {
    const options = {
        method,
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
    };

    if (data) {
        options.body = JSON.stringify(data);
    }

    return fetch(url, options)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .catch(error => {
            console.error('API Request Error:', error);
            throw error; // 向上拋出錯誤以便進一步處理
        });
}

// 登入功能
function login(e) {
    e.preventDefault();
    const account = document.getElementById('account').value;
    const password = document.getElementById('password').value;

    apiRequest('/user/api/login', 'POST', { account, password })
        .then(data => {
            console.log(data.message);
            if (data.status === 'success') {
                showLoginModal(data.message + `\n${account}`);
                showLoginView();
            } else {
                showLoginModal(data.message);
            }
        })
        .catch(error => {
            console.error('Login Error:', error);
        });
}

// 登入檢查功能
function loginCheck() {
    return apiRequest('/user/api/loginCheck', 'POST')
        .then(data => {
            if (data.memberId != null && data.account != null) {
                return data.account;
            } else {
                if (data.message === 'redirect') {
                    showLoginModal();
                    redirect();
                }
                return null;
            }
        })
        .catch(error => {
            console.error('Login Check Error:', error);
            return null;
        });
}

// 登出功能
function logout(e) {
    e.preventDefault();
    apiRequest('/user/api/logout', 'POST')
        .then(data => {
            if (data.status === 'success') {
                console.log(data.status);
                showLoginModal(data.message);
                showLoginView();
                window.location.href = "/user";
            }
            console.log(data.message);
        })
        .catch(error => {
            console.error('Logout Error:', error);
        });
}

// 處理重導
function redirect() {
    apiRequest('/user/api/redirect', 'POST')
        .then(data => {
            if (data.message) {
                console.log(data.message);
                window.location.href = data.message;
            }
        })
        .catch(error => {
            console.error('Logout Error:', error);
        });
}

function showLoginModal(Message) {
    let loginModal = document.querySelector('#loginModal');
    if (loginModal) {
        loginModal.remove();
    }

    let newDiv = document.createElement('div');
    newDiv.innerHTML = loginModalDiv;
    document.querySelector('main').appendChild(newDiv);
    loginMessage = Message;
    document.querySelector('#login-modal-body').innerHTML = (loginMessage) ? loginMessageDiv : loginFormView ; 
    if (loginMessage){
        document.querySelector('#loginMessage').textContent = loginMessage;
    }

    const modal = new bootstrap.Modal(document.querySelector('#loginModal'));
    modal.show();

    let loginForm = document.querySelector('#login_list');
    if (loginForm) {
        loginForm.removeEventListener('submit', login); // 先移除舊的事件
        loginForm.addEventListener('submit', login);
    }

    modal.hide();
}

function showLoginView() {
    loginCheck().then(account => {
        if (!account) {
            document.querySelector('header').innerHTML = notLoginNav;
            document.querySelector('#navLoginBtn').addEventListener('click',function(e){
                e.preventDefault();
                showLoginModal();
            })

        } else {
            document.querySelector('header').innerHTML = loginNav;
            console.log(account);

            let logoutButton = document.querySelector('#logout');
            if (logoutButton) {
                logoutButton.removeEventListener('click', logout);
                logoutButton.addEventListener('click', logout);
            }
        }
    });
}
