const form = document.getElementById('registrationForm');

// 表單驗證
form.addEventListener('submit', function (e) {
    e.preventDefault();
    registerMemberInfo(e)
});

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
        showLoginModal('密碼與確認密碼不符');
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
                showLoginModal('會員資料註冊成功！');
                setTimeout(function () {
                    window.location.href = "/user/";
                }, 3000);
            } else {
                let errorMessage = ""
                Object.keys(res).forEach(key => {
                    errorMessage += `${res[key]}<br>`;
                });
                showLoginModal(`<h4>註冊失敗：</h4>${errorMessage}`);
            }
        },
        error: function (xhr, status, error) {
            showLoginModal('<h4>會員資料註冊失敗：</h4>' + error);
        }
    });
}

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

