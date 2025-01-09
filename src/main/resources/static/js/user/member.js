function profileGetInfo() {
    return Promise.all([
        fetch('/user/api/memberInfo', { method: 'POST', credentials: 'include' }).then(res => res.json()),
        fetch('/user/api/avatar', { method: 'GET', credentials: 'include' }).then(res => res.blob())
    ])
        .then(([data, blob]) => {

            // 填充表單欄位
            document.querySelector('#email').value = data.account;
            document.querySelector('#lastName').value = data.lastName;
            document.querySelector('#firstName').value = data.firstName;
            document.querySelector('#gender').value = data.gender;
            document.querySelector('#birthday').value = data.birthday;
            document.querySelector('#phone').value = data.phone;

            // 處理頭像圖片
            if (blob.size != 0) {
                const imageUrl = URL.createObjectURL(blob);
                document.querySelector('#imagePreview').src = imageUrl;
                document.querySelector('#imagePreview').style.display = 'block';
                document.querySelector('.avatar-icon').style.display = 'none';
            }
        })
        .catch(error => {
            console.error('Get Member Info error:', error);
            return null;
        });
}

function updateMemberInfo(e) {
    const formData = new FormData();
    checkPassword();

    const memberInfo = {
        account: document.querySelector('#email').value,
        password: document.querySelector('#newPassword').value,
        lastName: document.querySelector('#lastName').value,
        firstName: document.querySelector('#firstName').value,
        gender: document.querySelector('#gender').value,
        birthday: document.querySelector('#birthday').value,
        phoneNumber: document.querySelector('#phone').value
    };

    formData.append('json', new Blob([JSON.stringify(memberInfo)], { type: 'application/json' }));

    const fileInput = document.querySelector('#imageUpload');
    if (fileInput.files.length > 0) {
        formData.append('file', fileInput.files[0]);
    }

    $.ajax({
        url: '/user/api/memberUpdate',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function(response) {
            alert('會員資料更新成功！');
            console.log("成功耶");
            console.log(response);
            profileGetInfo();
        },
        error: function(xhr, status, error) {
            console.log("失敗耶");
            console.log(error);
            alert('更新失敗：' + error);
        }
    });
}

profileGetInfo();

document.querySelector('#memberInfo').addEventListener('submit', function(e){
    e.preventDefault();
    updateMemberInfo();
});

document.querySelector('#removePic').addEventListener('click', function (e) {
    e.preventDefault();
    document.querySelector('#imagePreview').style.display = 'none';
    document.querySelector('.avatar-icon').style.display = 'block';
    document.querySelector('#imagePreview').src = "";
    document.querySelector('#imageUpload').value = "";
})

document.querySelector('#clear').addEventListener('click', function (e) {
        document.querySelector('#newPassword').value  = "";
        document.querySelector('#newPasswordAgain').value  = "";
        document.querySelector('#lastName').value  = "";
        document.querySelector('#firstName').value = "";
        document.querySelector('#gender').value  = "";
        document.querySelector('#birthday').value  = "";
        document.querySelector('#phone').value  = "";
});

function checkPassword(){
    let newPassword = document.querySelector('#newPassword').value;
    let newPasswordAgain = document.querySelector('#newPasswordAgain').value;

    if (newPassword != newPasswordAgain){
        showLoginModal("密碼不一致，請重新輸入");
        document.querySelector('#newPassword').value  = "";
        document.querySelector('#newPasswordAgain').value  = "";
        return true;
    }
}