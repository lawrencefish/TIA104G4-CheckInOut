const form = document.getElementById('contactUsForm');

// 表單驗證
form.addEventListener('submit', function (e) {
    e.preventDefault();
    contactUs(e)
});

function contactUs() {
    const formData = new FormData();
    const name = form.querySelector('input[name="name"]').value;
    const email = form.querySelector('input[name="email"]').value;
    const subject = form.querySelector('input[name="subject"]').value;
    const message = form.querySelector('input[name="message"]').value;
  
    const contactUs = {
		name: name,
		email: email,
		subject: subject,
		message: message
    };//

   if (password != confirmPassword) {
        showLoginModal('密碼與確認密碼不符');
        return;
    }

    console.log(contactUs);
    formData.append('json', new Blob([JSON.stringify(contactUs)], { type: 'application/json' }));

    const fileInput = document.querySelector('#imageUpload');
    if (fileInput.files.length > 0) {
        formData.append('file', fileInput.files[0]);
    }

    $.ajax({
        url: '/api/contactUs',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function (res) {
            if (res.success === 'success') {
                showLoginModal('表單成功送出！');
                setTimeout(function () {
                    window.location.href = "/contactUs/";
                }, 3000);
            } else {
                let errorMessage = ""
                Object.keys(res).forEach(key => {
                    errorMessage += `${res[key]}<br>`;
                });
                showLoginModal(`<h4>發送表單失敗</h4>${errorMessage}`);
            }
        },
        error: function (xhr, status, error) {
            showLoginModal('<h4>失敗：</h4>' + error);
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