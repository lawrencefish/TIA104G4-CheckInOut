const form = document.getElementById('registrationForm');


$(document).ready(function () {

    // 表單驗證
    form.addEventListener('submit', function (e) {
        e.preventDefault();
        registerMemberInfo(e)
        document.getElementById
    })

    document.querySelector('#removePic').addEventListener('click', function (e) {
        e.preventDefault();
        document.querySelector('#imagePreview').style.display = 'none';
        document.querySelector('.avatar-icon').style.display = 'block';
        document.querySelector('#imagePreview').src = "";
        document.querySelector('#imageUpload').value = "";
    })

    document.querySelector('#clear').addEventListener('click', function (e) {
        form.querySelector('input[name="last_name"]').value = "";
        form.querySelector('input[name="first_name"]').value = "";
        form.querySelector('input[name="account"]').value = "";
        form.querySelector('input[name="password"]').value = "";
        form.querySelector('input[name="confirm_password"]').value = "";
        form.querySelector('select[name="gender"]').value = "";
        form.querySelector('input[name="birthday"]').value = "";
        form.querySelector('input[name="phone_number"]').value = "";
    });


    $('#emailBtn').on('click',(e)=>{
        e.preventDefault();
        emailSend();

    })


});

function emailSend() {
    let email = $('#email').val();
    if (email !== null && email !=="") {
        $.post("/api/verification/member/send", { email: email }, function (response) {
            showModal(response);
            emailCheck();
        });
    } else {
        showModal("請輸入email");
    }
}

function emailCheck() {
    let email = $('#email').val();
    let html =`
                <div class="modal-content border-0">
                    <div class="modal-header border-0">
                        <h5 class="modal-title" id="emailVerifyModalLabel">輸入驗證碼</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body border-0">
                        <div class="input-group">
                            <input type="text" class="form-control" name="account" id="verify" required placeholder="請輸入驗證碼">
                            <button class="btn btn-outline-secondary" type="button" id="verifyEmail">驗證電子信箱</button>
                        </div>
                    </div>
                </div>
    `
    let vBadge = `
            <span class="input-group-text bg-white border-start-0">
                <span class="badge rounded-pill bg-success">v</span>
            </span>
    `
    showModal(html,false);
    setTimeout(() => {
        $(document).on('click', '#verifyEmail', function (e) {
            e.preventDefault();
            let verifyCode = $('#verify').val(); // 在點擊時重新獲取
            console.log(verifyCode);
            if (verifyCode !== null && verifyCode !== "") {
                $.post("/api/verification/register/check", { email: email, code: verifyCode }, function (response) {
                    let modalInstance = bootstrap.Modal.getInstance(document.getElementById("loginModal"));
                    if (modalInstance) {
                        modalInstance.hide();
                    }            
                    showModal(response.message);
                    console.log(response);
                    if(response.verify){
                        $('#email').after(vBadge);
                    }
                });
            } else {
                let modalInstance = bootstrap.Modal.getInstance(document.getElementById("loginModal"));
                if (modalInstance) {
                    modalInstance.hide();
                }        
                showModal("請輸入驗證碼");
                showModal(html,false);
            }
        });
    }, 500); 
}


function registerMemberInfo() {
    const formData = new FormData();
    const lastName = form.querySelector('input[name="last_name"]').value;
    const firstName = form.querySelector('input[name="first_name"]').value;
    const account = form.querySelector('input[name="account"]').value;
    const password = form.querySelector('input[name="password"]').value;
    const confirmPassword = form.querySelector('input[name="confirm_password"]').value;
    const gender = form.querySelector('select[name="gender"]').value;
    const birthday = form.querySelector('input[name="birthday"]').value;
    const phoneNumber = form.querySelector('input[name="phone_number"]').value;

    const memberInfo = {
        account: account,
        password: password,
        lastName: lastName,
        firstName: firstName,
        gender: gender,
        birthday: birthday,
        phoneNumber: phoneNumber
    };

    if (password != confirmPassword) {
        showModal('密碼與確認密碼不符');
        return;
    }

    console.log(memberInfo);
    formData.append('json', new Blob([JSON.stringify(memberInfo)], { type: 'application/json' }));

    const fileInput = document.querySelector('#imageUpload');
    if (fileInput.files.length > 0) {
        formData.append('file', fileInput.files[0]);
    }

    $.ajax({
        url: '/user/api/memberRegister',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function (res) {
            if (res.success === 'success') {
                showModal('會員資料註冊成功！送你一張新會員註冊優惠券');
                setTimeout(function () {
                    window.location.href = "/user/";
                }, 3000);
            } else {
                let errorMessage = ""
                Object.keys(res).forEach(key => {
                    errorMessage += `${res[key]}<br>`;
                });
                showModal(`<h4>註冊失敗：</h4>${errorMessage}`);
            }
        },
        error: function (xhr, status, error) {
            showModal('<h4>會員資料註冊失敗：</h4>' + error);
        }
    });
}

