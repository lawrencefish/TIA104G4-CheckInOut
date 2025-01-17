document.addEventListener('DOMContentLoaded', async () => {
    try {
        // 狀態轉換為中文
        const statusText = {
            0: '待審核',
            1: '已通過',
            2: '已拒絕'
        };
		
		let hotels = []; // 存儲實際的飯店數據
		let rooms = [];  // 存儲實際的房型數據
		
		// 獲取飯店列表
		async function fetchHotels() {
		    try {
		        const response = await fetch('/adminHotel/findAllHotels');
		        if (!response.ok) {
		            throw new Error(`HTTP錯誤 ! 狀態碼: ${response.status}`);
		        }
				const data = await response.json();
				console.log('已取得飯店資料:', data); // 添加日誌
		        return data;
		    } catch (error) {
		        console.error('取得飯店資料時發生錯誤:', error);
		        return [];
		    }
		}

        // 渲染業者列表
        function renderHotelsList(status = 'all') {
            const filteredHotels = status === 'all' 
                ? hotels 
                : hotels.filter(hotel => hotel.status === parseInt(status));
				
            const hotelsList = document.getElementById('hotelsList');
            hotelsList.innerHTML = filteredHotels.map(hotel => `
                <div class="review-card" data-hotel-id="${hotel.id}">
                    <img 
                        src="${hotel.image}" 
                        alt="${hotel.name}" 
                        class="card-image"
                    />
                    <div class="card-content">
                        <div class="card-header">
                            <h3 class="card-title">${hotel.name}</h3>
                            <span class="status-badge status-${hotel.status}">${statusText[hotel.status]}</span>
                        </div>
                        <p class="card-info">地址：${hotel.address}</p>
                        <p class="card-info">統一編號：${hotel.taxId}</p>
                        <p class="card-info">提交日期：${hotel.submitDate}</p>
                        <div class="card-actions">
                            <button class="action-button primary-button">審核詳情</button>
                        </div>
                    </div>
                </div>
            `).join('');

            // 添加點擊事件
            document.querySelectorAll('.review-card').forEach(card => {
                card.querySelector('.action-button').addEventListener('click', (e) => {
					e.preventDefault();
                        const hotelId = card.dataset.hotelId;
                    if (hotelId) {
                        window.location.href = `/admin/industryReview?/${hotelId}`;
                    } else {
						console.error('查無飯店ID');
					}
                });
            });
        }

		
		// 獲取房型列表
   		async function fetchRooms() {
   			try {
           		const response = await fetch('/adminRoomType/findAllRooms');
           		if (!response.ok) {
               		throw new Error(`HTTP錯誤 ! 狀態碼: ${response.status}`);
           		}
       			const data = await response.json();
           		console.log('已取得房型資料:', data);
           		return data;
       		} catch (error) {
           		console.error('取得房型資料時發生錯誤:', error);
           		return [];
			}
		}
        // 渲染房型列表
        function renderRoomsList(status = 'all') {
            const filteredRooms = status === 'all'
                ? rooms
                : rooms.filter(room => room.status === parseInt(status));

            const roomsList = document.getElementById('roomsList');
            roomsList.innerHTML = filteredRooms.map(room => `
                <div class="review-card" data-room-id="${room.roomTypeId}">
                    <img 
                        src="${room.image}" 
                        alt="${room.roomName}" 
                        class="card-image"
                    />
                    <div class="card-content">
                        <div class="card-header">
                            <h3 class="card-title">${room.roomName}</h3>
                            <span class="status-badge status-${room.status}">${statusText[room.status]}</span>
                        </div>
                        <p class="card-info">飯店名稱：${room.hotel.name}</p>
						<p class="card-info">最大入住人數：${room.maxPerson}人</p>
						<p class="card-info">房間數量：${room.roomNum}</p>
						<p class="card-info">早餐：${room.breakfast === 1 ? '含早餐' : '不含早餐'}</p>
                        <p class="card-info">提交日期：${new Date(room.reviewTime).toLocaleDateString()}</p>
                        <div class="card-actions">
                            <button class="action-button primary-button">審核詳情</button>
                        </div>
                    </div>
                </div>
            `).join('');

            // 添加點擊事件
            document.querySelectorAll('.review-card').forEach(card => {
				const actionButton = card.querySelector('.action-button');
                actionButton.addEventListener('click', (e) => {
					e.preventDefault();
					console.log('按鈕點擊');
                    const roomId = card.dataset.roomId;
					console.log('房型ID', roomId);
					if (roomId){
                        const redirectUrl = `/adminRoomType/roomtypeReview/${roomId}`;
						console.log('準備跳轉到:', redirectUrl);
						window.location.href = redirectUrl;
                    } else {
						console.error('查無房型ID')
					}
                });
            });
        }

        // 切換頁籤邏輯
        function switchTab(tab) {
            const hotelsTab = document.getElementById('hotelsTab');
            const roomsTab = document.getElementById('roomsTab');
            const hotelsList = document.getElementById('hotelsList');
            const roomsList = document.getElementById('roomsList');

            if (tab === 'hotels') {
                hotelsTab.classList.add('active-tab');
                hotelsTab.classList.remove('inactive-tab');
                roomsTab.classList.add('inactive-tab');
                roomsTab.classList.remove('active-tab');
                hotelsList.style.display = 'grid';
                roomsList.style.display = 'none';
            } else {
                hotelsTab.classList.remove('active-tab');
                hotelsTab.classList.add('inactive-tab');
                roomsTab.classList.remove('inactive-tab');
                roomsTab.classList.add('active-tab');
                hotelsList.style.display = 'none';
                roomsList.style.display = 'grid';
            }
        }

        // 初始化事件監聽
        document.getElementById('hotelsTab').addEventListener('click', () => switchTab('hotels'));
        document.getElementById('roomsTab').addEventListener('click', () => switchTab('rooms'));

        // 計算並更新狀態數量
        function updateStatusCounts(items) {
            const counts = {
                all: items.length,
                0: items.filter(item => item.status === 0).length, // 待審核
                1: items.filter(item => item.status === 1).length, // 已通過
                2: items.filter(item => item.status === 2).length  // 已拒絕
            };

            // 更新數量顯示
            document.querySelector('[data-status="all"] .count-badge').textContent = counts.all;
            document.querySelector('[data-status="0"] .count-badge').textContent = counts[0];
            document.querySelector('[data-status="1"] .count-badge').textContent = counts[1];
            document.querySelector('[data-status="2"] .count-badge').textContent = counts[2];
        }

        // 初始化狀態篩選功能
        function initializeFilters() {
            const filterButtons = document.querySelectorAll('.filter-chip');
            let currentTab = 'hotels';
            let currentStatus = 'all';

            // 初始化狀態數量
//            updateStatusCounts(hotels);

            // 更新篩選按鈕狀態
            function updateFilterButtons(selectedStatus) {
                filterButtons.forEach(button => {
                    if (button.dataset.status === selectedStatus) {
                        button.style.backgroundColor = '#e2e8f0';
                        button.style.fontWeight = '600';
                    } else {
                        button.style.backgroundColor = 'transparent';
                        button.style.fontWeight = 'normal';
                    }
                });
            }

            // 添加篩選事件監聽器
            filterButtons.forEach(button => {
                button.addEventListener('click', () => {
                    const status = button.dataset.status;
                    currentStatus = status;
                    updateFilterButtons(status);

                    // 根據當前頁籤更新列表
                    if (currentTab === 'hotels') {
                        renderHotelsList(status);
                    } else {
                        renderRoomsList(status);
                    }
                });
            });

            // 更新頁籤切換邏輯
			document.getElementById('hotelsTab').addEventListener('click', () => {
                currentTab = 'hotels';
                switchTab('hotels');
                renderHotelsList(currentStatus);
                updateStatusCounts(hotels);
            });

            document.getElementById('roomsTab').addEventListener('click', () => {
                currentTab = 'rooms';
                switchTab('rooms');
                renderRoomsList(currentStatus);
                updateStatusCounts(rooms);
            });

        }
		// 初始化數據和功能
        hotels = await fetchHotels();
		rooms = await fetchRooms();
        renderHotelsList('all');
        renderRoomsList('all');
        updateStatusCounts(hotels);
        initializeFilters();

    } catch (error) {
        console.error('Error initializing review page:', error);
    }
});