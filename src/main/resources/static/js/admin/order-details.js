// 假資料
const mockData = {
    member: {
        avatar: './img/會員頭像.jpg',
        account: 'Godoflol@lol.com',
        gender: '男',
        firstName: '李',
        lastName: '相赫',
        birthday: '1996/05/07',
        phone: '091234-5678',
        email: 'Godoflol@lol.com'
    },
    order: {
        orderNumber: 'ORD20240315001',
        status: '已確認',
        hotelImage: 'hotel-image.jpg',
        hotelName: '台中金典酒店',
        roomType: '總統套房',
        roomInfo: {
            summary: '50坪 / 市景 / 2張大床',
            quantity: '1間',
            facilities: '免費WiFi、停車場、游泳池'
        },
        stayInfo: {
            checkIn: '2024-03-20',
            checkOut: '2024-03-22',
            guests: '2位大人'
        },
        guestInfo: {
            name: '李相赫',
            phone: '0912-345-678',
            email: 'faker@t1.com'
        },
        paymentInfo: {
            method: '信用卡支付 (末四碼: 5678)',
            coupon: '首次入住優惠券',
            totalAmount: '$19,500'
        },
        review: {
            rating: 5,
            date: '2024-03-25',
            content: '服務很好，房間乾淨舒適，景觀非常棒！推薦給大家。',
            hotelResponse: {
                content: '感謝您的評價，期待您下次再度光臨！',
                date: '2024-03-26'
            }
        }
    }
};

document.addEventListener('DOMContentLoaded', () => {
    // 載入 Header 和 Footer
    loadHeaderAndFooter();
    
    // 載入會員資料
    loadMemberData();
    
    // 載入訂單資料
    loadOrderData();
    
    // 返回按鈕功能
    setupBackButton();
});

// 載入 Header 和 Footer
function loadHeaderAndFooter() {
    fetch('backend-header.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('header').innerHTML = data;
        });

    fetch('backend-footer.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('footer').innerHTML = data;
        });
}

// 載入會員資料
function loadMemberData() {
    const member = mockData.member;
    document.querySelector('.avatar').src = member.avatar;
    document.querySelector('#account .content').textContent = member.account;
    document.querySelector('#gender .content').textContent = member.gender;
    document.querySelector('#first-name .content').textContent = member.firstName;
    document.querySelector('#last-name .content').textContent = member.lastName;
    document.querySelector('#birthday .content').textContent = member.birthday;
    document.querySelector('#phone .content').textContent = member.phone;
    document.querySelector('#email .content').textContent = member.email;
}

// 載入訂單資料
function loadOrderData() {
    const order = mockData.order;
    
    // 更新訂單基本資訊
    document.querySelector('.order-number').textContent = `訂單編號：${order.orderNumber}`;
    document.querySelector('.order-status').textContent = order.status;
    document.querySelector('.order-image').src = order.hotelImage;
    
    // 更新飯店和房型資訊
    document.querySelector('.hotel-name').textContent = order.hotelName;
    document.querySelector('.room-type').textContent = order.roomType;
    
    // 更新房型資訊
    const roomInfoValues = document.querySelectorAll('.room-summary .order-info-value');
    roomInfoValues[0].textContent = order.roomInfo.summary;
    roomInfoValues[1].textContent = order.roomInfo.quantity;
    roomInfoValues[2].textContent = order.roomInfo.facilities;
    
    // 更新住宿資訊
    const stayInfoValues = document.querySelectorAll('.stay-info .order-info-value');
    stayInfoValues[0].textContent = order.stayInfo.checkIn;
    stayInfoValues[1].textContent = order.stayInfo.checkOut;
    stayInfoValues[2].textContent = order.stayInfo.guests;
    
    // 更新住客資料
    const guestInfoValues = document.querySelectorAll('.guest-info .order-info-value');
    guestInfoValues[0].textContent = order.guestInfo.name;
    guestInfoValues[1].textContent = order.guestInfo.phone;
    guestInfoValues[2].textContent = order.guestInfo.email;
    
    // 更新付款資訊
    const paymentInfoValues = document.querySelectorAll('.payment-info .order-info-value');
    paymentInfoValues[0].textContent = order.paymentInfo.method;
    paymentInfoValues[1].textContent = order.paymentInfo.coupon;
    paymentInfoValues[2].textContent = order.paymentInfo.totalAmount;
}

// 設置返回按鈕功能
function setupBackButton() {
    document.getElementById('backButton').addEventListener('click', () => {
        window.history.back();
    });
}

// 切換評論類型
const reviewButtons = document.querySelectorAll('.review-buttons button');
const reviewContent = document.querySelector('.review-content');

reviewButtons.forEach(button => {
  button.addEventListener('click', () => {
    reviewButtons.forEach(btn => btn.classList.remove('active'));
    button.classList.add('active');

    // 根據按鈕顯示相應的評論內容
    if (button.textContent === '業者評論') {
      // 顯示業者評論
      reviewContent.innerHTML = `
        <div class="review-item">
          <img src="profile-pic.jpg" alt="Profile Picture" class="review-pic">
          <div class="review-info">
            <h3 class="review-name">Jane Doe</h3>
            <div class="review-stars">★★★★☆</div>
            <p class="review-text">Excellent service, highly recommended!</p>
          </div>
        </div>
      `;
    } else {
      // 顯示來自業者的評論
      reviewContent.innerHTML = `
        <div class="review-item">
          <img src="provider-pic.jpg" alt="Provider Picture" class="review-pic">
          <div class="review-info">
            <h3 class="review-name">Provider Name</h3>
            <div class="review-stars">★★★★★</div>
            <p class="review-text">Thank you for your business, we appreciate your support!</p>
          </div>
        </div>
      `;
    }
  });
});