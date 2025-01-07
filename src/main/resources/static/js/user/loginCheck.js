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
        <li><a href="/user/orderedfinal" class="nav-link px-2 link-body-emphasis">訂單查詢</a></li>
        <li><a href="#" class="nav-link px-2 link-body-emphasis">購物車</a></li>
        <li>
            <div class="dropdown">
                <a href="#" class="d-block link-body-emphasis text-decoration-none px-2 py-2"
                    data-bs-toggle="dropdown" aria-expanded="false">登入 / 註冊
                </a>
                <div class="dropdown-menu dropdown-menu-end mt-3 px-3 border-0 shadow-sm">
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
                            <button class="btn btn-primary login" type="submit">Sign in</button>
                            <button class="btn btn-primary" type="button">Sign in with Google</button>
                        </div>
                    </form>
                </div>
            </div>
        </li>
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
                <li><a href="/user/orderedfinal" class="nav-link px-2 link-body-emphasis">訂單查詢</a></li>
                <li><a href="#" class="nav-link px-10 link-body-emphasis">購物車</a></li>
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
let loginMessage = "";
const loginModal = `
	<div class="modal fade" id="loginModal" tabindex="-1" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered" style="max-width:25vw;">
			<div class="modal-content text-center">
				<div class="modal-body">
					<!-- 登入結果訊息 -->
					<p id="loginMessage" class="fs-5 mb-3"></p>
					<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">確定</button>
				</div>
			</div>
		</div>
	</div>
`

document.addEventListener('DOMContentLoaded', function () {
    showLoginView();
});

function login(e) {
    e.preventDefault();
    const account = document.getElementById('account').value;
    const password = document.getElementById('password').value;

    fetch('/user/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ account, password }),
        credentials: 'include',
    })
        .then(response => response.json())
        .then(data => {
            console.log(data.message);
            if (data.status === 'success') {
                showLoginModal(data.message+`\n${account}`);
                showLoginView();
            } else {
                showLoginModal(data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function loginCheck() {
    return fetch('/user/api/loginCheck', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (data.memberId != null && data.account != null) {
                return data.account;
            } else {
                return null;
            }
        })
        .catch(error => {
            console.error('Error:', error);
            return null;
        });
}

function logout(e) {
    e.preventDefault();
    fetch('/user/api/logout', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                console.log(data.status);
                showLoginModal(data.message);
                showLoginView();
            }
            console.log(data.message);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function showLoginModal(Message) {
    const LoginModal = document.querySelector('#loginModal');
    if (!LoginModal) {
        const newDiv = document.createElement('div');
        newDiv.innerHTML = loginModal;
        document.querySelector('main').appendChild(newDiv);
    }
    document.querySelector('#loginMessage').textContent = Message;
    const modal = new bootstrap.Modal(document.querySelector('#loginModal'));
    modal.show();
}

function showLoginView() {
    loginCheck().then(account => {
        if (!account) {
            document.querySelector('header').innerHTML = notLoginNav;
            document.querySelector('#login_list').addEventListener('submit', function (e) {
                login(e);
            });
        } else {
            document.querySelector('header').innerHTML = loginNav;
            console.log(account);
            document.querySelector('#logout').addEventListener('click', function (e) {
                logout(e);
            })
        }
    });
}