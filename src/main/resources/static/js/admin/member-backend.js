document.addEventListener('DOMContentLoaded', async () => {
    try {
        // 載入 Header 和 Footer
        await loadHeaderAndFooter();
        
        // 載入會員和訂單資料
        await loadMemberAndOrderData();
        
    } catch (error) {
        console.error('載入資料失敗:', error);
        alert('載入資料失敗，請稍後再試');
    }
});

// 假資料 - 之後可替換成資料庫資料
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
    orders: [
        {
            id: 'ORD20240315001',
            hotelImage: './img/hotel1.jpg',
            hotelName: '台中金典酒店',
            roomType: '總統套房',
            checkIn: '2024-03-20',
            checkOut: '2024-03-22',
            guests: '2位大人',
            price: 19500
        },
        {
            id: 'ORD20240315002',
            hotelImage: './img/hotel2.jpg',
            hotelName: '台北圓山大飯店',
            roomType: '總統套房',
            checkIn: '2024-04-15',
            checkOut: '2024-04-17',
            guests: '2位大人',
            price: 24500
        },
        {
            id: 'ORD20240315003',
            hotelImage: './img/hotel3.jpg',
            hotelName: '台南超級大酒店',
            roomType: '總統套房',
            checkIn: '2024-05-01',
            checkOut: '2024-05-03',
            guests: '2位大人',
            price: 15400
        }
    ]
};

// 載入會員和訂單資料
async function loadMemberAndOrderData() {
    try {
        // 這裡可以替換成實際的 API 呼叫
        // const response = await fetch('/api/member/data');
        // const data = await response.json();
        
        // 暫時使用假資料
        const data = mockData;
        
        // 載入會員資料
        loadMemberData(data.member);
        
        // 載入訂單資料
        loadOrdersList(data.orders);
        
    } catch (error) {
        console.error('獲取資料失敗:', error);
        throw error;
    }
}

// 載入會員資料
function loadMemberData(memberData) {
    document.querySelector('.avatar').src = memberData.avatar;
    document.querySelector('#account .content').textContent = memberData.account;
    document.querySelector('#gender .content').textContent = memberData.gender;
    document.querySelector('#first-name .content').textContent = memberData.firstName;
    document.querySelector('#last-name .content').textContent = memberData.lastName;
    document.querySelector('#birthday .content').textContent = memberData.birthday;
    document.querySelector('#phone .content').textContent = memberData.phone;
    document.querySelector('#email .content').textContent = memberData.email;
}

// 載入訂單列表
function loadOrdersList(orders) {
    const ordersContainer = document.querySelector('.orders');
    // 保留標題
    const title = ordersContainer.querySelector('h3');
    // 清空現有內容
    ordersContainer.innerHTML = '';
    // 重新加入標題
    ordersContainer.appendChild(title);

    orders.forEach(order => {
        const orderElement = createOrderElement(order);
        ordersContainer.appendChild(orderElement);
    });
}

// 創建訂單元素
function createOrderElement(order) {
    const orderContainer = document.createElement('div');
    orderContainer.className = 'order-container';
    orderContainer.innerHTML = `
        <img src="${order.hotelImage}" alt="${order.hotelName}" class="order-image">
        <div class="order-details">
            <h3>${order.hotelName} - ${order.roomType}</h3>
            <p>入住日期：${order.checkIn}</p>
            <p>退房日期：${order.checkOut}</p>
            <p>入住人數：${order.guests}</p>
            <p class="price">$${order.price.toLocaleString()}</p>
        </div>
    `;

    // 添加點擊事件導向訂單詳情頁
    orderContainer.addEventListener('click', () => {
        window.location.href = `order-details.html?orderId=${order.id}`;
    });

    return orderContainer;
}

// 載入 Header 和 Footer
async function loadHeaderAndFooter() {
    try {
        const [headerResponse, footerResponse] = await Promise.all([
            fetch('backend-header.html'),
            fetch('backend-footer.html')
        ]);

        const headerText = await headerResponse.text();
        const footerText = await footerResponse.text();

        document.getElementById('header').innerHTML = headerText;
        document.getElementById('footer').innerHTML = footerText;

    } catch (error) {
        console.error('載入 Header 或 Footer 失敗:', error);
        throw error;
    }
}

// 當要改成使用資料庫時的範例：
/*
async function loadMemberAndOrderData() {
    try {
        // 獲取會員資料
        const memberResponse = await fetch('/api/member/current', {
            headers: {
                'Authorization': `Bearer ${getToken()}` // 假設使用 JWT
            }
        });
        const memberData = await memberResponse.json();

        // 獲取訂單列表
        const ordersResponse = await fetch('/api/member/orders', {
            headers: {
                'Authorization': `Bearer ${getToken()}`
            }
        });
        const ordersData = await ordersResponse.json();

        // 載入資料到頁面
        loadMemberData(memberData);
        loadOrdersList(ordersData);

    } catch (error) {
        console.error('獲取資料失敗:', error);
        throw error;
    }
}
*/