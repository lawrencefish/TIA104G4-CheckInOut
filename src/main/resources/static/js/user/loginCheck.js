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
                            data-bs-toggle="dropdown" aria-expanded="false"> <img id="avatarImg" src=""
                                alt="mdo" width="45" height="45" class="rounded-circle">
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
        <div class="modal-dialog modal-dialog-centered modal-xl">  <!-- 這裡使用 modal-xl -->
			<div class="modal-content text-center">
				<div class="modal-body" id="login-modal-body">
				</div>
			</div>
		</div>
	</div>
`

let loginMessage = "";

const loginMessageDiv =`
<div id="loginMessage" class="fs-6 mb-3"></div>
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
            <a class="btn btn-primary" id="register" type="button" href="/user/register">註冊</a>
        </div>
    </form>
`;

let footer = `
		<div class="row border-top">
			<div class="container">
				<ul class="nav justify-content-center">
					<li class="nav-item"><a href="/user/" class="nav-link px-2 text-body-secondary">首頁</a></li>
					<li class="nav-item"><a	href="/user/cart" class="nav-link px-2 text-body-secondary">購物車</a></li>
					<li class="nav-item"><a href="/user/news" class="nav-link px-2 text-body-secondary">最新消息</a></li>
					<li class="nav-item"><a href="/user/order" class="nav-link px-2 text-body-secondary">訂單管理</a>
					</li>
					<li class="nav-item"><a href="/user/faq" class="nav-link px-2 text-body-secondary">常見問題</a>
					</li>
					<li class="nav-item"><a href="/user/contactUs"
							class="nav-link px-2 text-body-secondary">聯繫我們</a></li>
					<li class="nav-item"><a href="/login/" class="nav-link px-2 text-body-secondary">夥伴專區</a></li>
				</ul>
				<div class="d-flex justify-content-center align-items-start mb-0">
					<img src="/imgs/user/checKInOut_logo_light.png" alt="checkinout" width="128">
				</div>
				<p class="text-center text-body-secondary">&copy; 2024 Check IN OUT</p>
			</div>
		</div>
`

let redirectUrl ="";

document.addEventListener('DOMContentLoaded', function () {
    showLoginView();
    document.querySelector('footer').innerHTML = footer;
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
            if (data.status === 'success') {
                showLoginView();

                const loginModal = bootstrap.Modal.getInstance(document.querySelector('#loginModal'));
                if (loginModal) {
                    loginModal.hide();
                }

                showModal(data.message);
                if (redirectUrl){
                    window.location.href = redirectUrl;
                    redirectUrl = "";
                }
            } else {
                showModal(data.message);
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
                if(window.location.pathname == '/user/register'){
                    showModal("你已經是會員咯！");
                    setTimeout(function() {
                        window.location.href = "/user/";
                    }, 2000);     
                }
                return data.account;
            } else {
                if (data.url) {
                    redirectUrl = data.url;
                    showModal();
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
                showModal("登出成功，三秒後跳轉到首頁");
                showLoginView();    
                setTimeout(function() {
                    window.location.href = "/user/";
                }, 3000); 
            }
        })
        .catch(error => {
            console.error('Logout Error:', error);
        });
}

function getAvatar(){
    fetch('/user/api/avatar', {
        method: 'GET',
        credentials: 'include' 
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch avatar');
            }
            return response.blob();
        })
        .then(blob => {
            const imageUrl = URL.createObjectURL(blob); 
            console.log(imageUrl);
            document.getElementById('avatarImg').src = imageUrl; 
        })
        .catch(error => {
            console.error('Error fetching avatar:', error);
        });
    
}

let modalQueue = []; // 建立一個佇列來存放訊息
let isModalVisible = false; // 追蹤模態視窗是否正在顯示

function showModal(message = null, isMessage = true) {
    modalQueue.push({ message, isMessage }); // 將訊息及模式加入佇列
    processModalQueue(); // 處理佇列
}

function processModalQueue() {
    if (isModalVisible || modalQueue.length === 0) return;

    isModalVisible = true;
    let { message, isMessage } = modalQueue.shift();

    let loginModal = document.querySelector('#loginModal');
    
    if (!loginModal) {
        let newDiv = document.createElement('div');
        newDiv.innerHTML = loginModalDiv;
        document.querySelector('main').appendChild(newDiv);
    }

    const modalBody = document.querySelector('#login-modal-body');
    if (modalBody) {
        if (message === null) {
            modalBody.innerHTML = loginFormView;
        } else if (isMessage) {
            modalBody.innerHTML = loginMessageDiv; 
            document.querySelector('#loginMessage').innerHTML = message;
        } else {
            modalBody.innerHTML = message; // 直接更改 innerHTML
        }
    }

    const existingModalInstance = bootstrap.Modal.getInstance(document.querySelector('#loginModal'));
    if (existingModalInstance) {
        existingModalInstance.dispose();
    }

    const modalInstance = new bootstrap.Modal(document.querySelector('#loginModal'), { keyboard: true });
    modalInstance.show();

    // 當模態視窗關閉時，標記 isModalVisible = false，並繼續處理佇列
    document.querySelector('#loginModal').addEventListener('hidden.bs.modal', function () {
        isModalVisible = false;
        processModalQueue();
    }, { once: true });

    // 綁定登入表單事件
    let loginForm = document.querySelector('#login_list');
    if (loginForm) {
        loginForm.removeEventListener('submit', login);
        loginForm.addEventListener('submit', login);
    }
    
    document.querySelector('#loginModal').addEventListener('hidden.bs.modal', function () {
        redirectUrl = "";
    });
}


function showLoginView() {
    loginCheck().then(account => {
        if (!account) {
            document.querySelector('header').innerHTML = notLoginNav;

            let loginBtn = document.querySelector('#navLoginBtn');
            if (loginBtn) {
                loginBtn.addEventListener('click', function (e) {
                    e.preventDefault();
                    showModal();
                });
            }

        } else {
            document.querySelector('header').innerHTML = loginNav;
            getAvatar();
            let logoutButton = document.querySelector('#logout');
            if (logoutButton) {
                logoutButton.removeEventListener('click', logout);
                logoutButton.addEventListener('click', logout);
            }
        }
    });
}
