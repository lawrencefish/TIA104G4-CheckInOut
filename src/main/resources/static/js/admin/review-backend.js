document.addEventListener('DOMContentLoaded', async () => {
    try {
        const ITEMS_PER_PAGE = 6;
        let currentPage = 1;
        let currentTab = 'hotels';
        let currentStatus = 'all';
        let hotels = []; // 存儲飯店數據
        let rooms = [];  // 存儲房型數據

        // 狀態轉換為中文
        const statusText = {
            0: '待審核',
            1: '已通過',
            2: '已拒絕'
        };

        // 獲取飯店列表
		async function fetchHotels() {
            try {
                const response = await fetch('/adminHotel/findAllHotels');
                if (!response.ok) {
                    throw new Error(`HTTP錯誤 ! 狀態碼: ${response.status}`);
                }
                const data = await response.json();
                
                // 為每個飯店獲取首張圖片
                const hotelsWithImages = await Promise.all(data.map(async hotel => {
                    try {
                        const imgResponse = await fetch(`/adminHotel/firstImage/${hotel.hotelId}`);
                        if (imgResponse.ok) {
                            const imageBlob = await imgResponse.blob();
                            hotel.imageUrl = URL.createObjectURL(imageBlob);
                        } else {
                            hotel.imageUrl = '/static/imgs/default-hotel.png'; // 預設圖片
                        }
                    } catch (error) {
                        console.error('獲取飯店圖片錯誤:', error);
                        hotel.imageUrl = '/static/imgs/default-hotel.png';
                    }
                    return hotel;
                }));
                
                console.log('已取得飯店資料:', hotelsWithImages);
                return hotelsWithImages;
            } catch (error) {
                console.error('取得飯店資料時發生錯誤:', error);
                return [];
            }
        }

        // 獲取房型列表
		async function fetchRooms() {
            try {
                const response = await fetch('/adminRoomType/findAllRooms');
                if (!response.ok) {
                    throw new Error(`HTTP錯誤 ! 狀態碼: ${response.status}`);
                }
                const data = await response.json();
				console.log('API回傳的原始房型資料：', data); // 檢查 API 回傳的資料
				
                // 為每個房型獲取首張圖片
                const roomsWithImages = await Promise.all(data.map(async room => {
                    try {
                        const imgResponse = await fetch(`/adminRoomType/firstImage/${room.roomTypeId}`);
                        if (imgResponse.ok) {
                            const imageBlob = await imgResponse.blob();
                            room.imageUrl = URL.createObjectURL(imageBlob);
                        } else {
                            room.imageUrl = '/static/imgs/default-room.png'; // 預設圖片
                        }
                    } catch (error) {
                        console.error('獲取房型圖片錯誤:', error);
                        room.imageUrl = '/static/imgs/default-room.png';
                    }
                    return room;
                }));
                
                console.log('已取得房型資料:', roomsWithImages);
                return roomsWithImages;
            } catch (error) {
                console.error('取得房型資料時發生錯誤:', error);
                return [];
            }
        }

        // 分頁函數 - 只返回當前頁面需要的數據
        function getPageItems(items, page) {
            const start = (page - 1) * ITEMS_PER_PAGE;
            return items.slice(start, start + ITEMS_PER_PAGE);
        }

        // 一次性創建分頁控制器
        function setupPagination() {
            const paginationDiv = document.createElement('div');
            paginationDiv.className = 'pagination';
            paginationDiv.innerHTML = `
                <button class="page-btn prev-btn">上一頁</button>
                <span class="page-info"></span>
                <button class="page-btn next-btn">下一頁</button>
            `;
            
            document.querySelector('.cards-grid').after(paginationDiv);
            
            const prevBtn = paginationDiv.querySelector('.prev-btn');
            const nextBtn = paginationDiv.querySelector('.next-btn');
            
            prevBtn.addEventListener('click', () => {
                if (currentPage > 1) {
                    currentPage--;
                    refreshCurrentView();
                }
            });
            
            nextBtn.addEventListener('click', () => {
                const totalPages = Math.ceil(getCurrentItems().length / ITEMS_PER_PAGE);
                if (currentPage < totalPages) {
                    currentPage++;
                    refreshCurrentView();
                }
            });
        }

        // 更新分頁控制器狀態
        function updatePaginationState() {
            const items = getCurrentItems();
            const totalPages = Math.ceil(items.length / ITEMS_PER_PAGE);
            
            const pagination = document.querySelector('.pagination');
            if (!pagination) return;
            
            const prevBtn = pagination.querySelector('.prev-btn');
            const nextBtn = pagination.querySelector('.next-btn');
            const pageInfo = pagination.querySelector('.page-info');
            
            prevBtn.disabled = currentPage === 1;
            nextBtn.disabled = currentPage === totalPages;
            pageInfo.textContent = `${currentPage} / ${totalPages}`;
            
            pagination.style.display = items.length > ITEMS_PER_PAGE ? 'flex' : 'none';
        }

        // 獲取當前篩選後的項目
        function getCurrentItems() {
            const items = currentTab === 'hotels' ? hotels : rooms;
            return currentStatus === 'all' 
                ? items 
                : items.filter(item => item.status === parseInt(currentStatus));
        }

        // 刷新當前視圖
        function refreshCurrentView() {
            if (currentTab === 'hotels') {
                renderHotelsList(currentStatus);
            } else {
                renderRoomsList(currentStatus);
            }
            updatePaginationState();
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

        // 渲染業者列表
		function renderHotelsList(status = 'all') {
            const filteredHotels = status === 'all' 
                ? hotels 
                : hotels.filter(hotel => hotel.status === parseInt(status));
            
            const pageHotels = getPageItems(filteredHotels, currentPage);
            
            const hotelsList = document.getElementById('hotelsList');
            hotelsList.innerHTML = pageHotels.map(hotel => `
                <div class="review-card" data-hotel-id="${hotel.hotelId}">
                    <img 
                        src="${hotel.imageUrl}" 
                        alt="${hotel.hotelName}" 
                        class="card-image"
                        loading="lazy"
                        onerror="this.src='/static/images/default-hotel.jpg'"
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

            // 使用事件委託處理點擊
            hotelsList.addEventListener('click', (e) => {
                if (e.target.classList.contains('action-button')) {
                    e.preventDefault();
                    const card = e.target.closest('.review-card');
                    if (card) {
                        const hotelId = card.dataset.hotelId;
                        if (hotelId) {
                            window.location.href = `/admin/industryReview?hotelId=${hotelId}`;
                        }
                    }
                }
            });
            
            updatePaginationState();
        }

        // 渲染房型列表
		function renderRoomsList(status = 'all') {
			console.log('全部房型資料：', rooms); // 檢查 rooms 陣列內容
			
            const filteredRooms = status === 'all'
                ? rooms
                : rooms.filter(room => room.status === parseInt(status));
            
            const pageRooms = getPageItems(filteredRooms, currentPage);
            
            const roomsList = document.getElementById('roomsList');
            roomsList.innerHTML = pageRooms.map(room => {
				
					console.log('目前處理的房型：', room); // 檢查個別房型資料
					// 從 roomType 取得房型資料，從 hotelName 取得飯店名稱
			        const roomData = room.roomType || room;
			        const hotelName = room.hotelName || '未指定';
			        
					return `
			            <div class="review-card" data-room-id="${roomData.roomTypeId}">
			                <img 
			                    src="${roomData.imageUrl}" 
			                    alt="${roomData.roomName}" 
			                    class="card-image"
			                    loading="lazy"
			                    onerror="this.src='/static/images/default-room.jpg'"
			                />
			                <div class="card-content">
			                    <div class="card-header">
			                        <h3 class="card-title">${roomData.roomName}</h3>
			                        <span class="status-badge status-${roomData.status}">${statusText[roomData.status]}</span>
			                    </div>
			                    <p class="card-info">飯店名稱：${hotelName}</p>
			                    <p class="card-info">最大入住人數：${roomData.maxPerson}人</p>
			                    <p class="card-info">房間數量：${roomData.roomNum}</p>
			                    <p class="card-info">早餐：${roomData.breakfast === 1 ? '含早餐' : '不含早餐'}</p>
			                    <p class="card-info">提交日期：${new Date(roomData.reviewTime).toLocaleDateString()}</p>
			                    <div class="card-actions">
			                        <button class="action-button primary-button">審核詳情</button>
			                    </div>
			                </div>
			            </div>
			        `;
			    }).join('');

            // 使用事件委託處理點擊
            roomsList.addEventListener('click', (e) => {
                if (e.target.classList.contains('action-button')) {
                    e.preventDefault();
                    const card = e.target.closest('.review-card');
                    if (card) {
                        const roomId = card.dataset.roomId;
                        if (roomId) {
                            const redirectUrl = `/adminRoomType/roomtypeReview/${roomId}`;
                            fetch(`/adminRoomType/detail/${roomId}`)
                                .then(response => {
                                    if (!response.ok) throw new Error('房型不存在');
                                    window.location.href = redirectUrl;
                                })
                                .catch(() => alert('無法找到該房型資訊，請稍後再試'));
                        }
                    }
                }
            });
            
            updatePaginationState();
        }

        // 更新狀態數量顯示
        function updateStatusCounts(items) {
            const counts = {
                all: items.length,
                0: items.filter(item => item.status === 0).length,
                1: items.filter(item => item.status === 1).length,
                2: items.filter(item => item.status === 2).length
            };

            document.querySelector('[data-status="all"] .count-badge').textContent = counts.all;
            document.querySelector('[data-status="0"] .count-badge').textContent = counts[0];
            document.querySelector('[data-status="1"] .count-badge').textContent = counts[1];
            document.querySelector('[data-status="2"] .count-badge').textContent = counts[2];
        }

        // 更新篩選按鈕狀態
        function updateFilterButtons(selectedStatus) {
            const filterButtons = document.querySelectorAll('.filter-chip');
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

        // 初始化狀態篩選功能
        function initializeFilters() {
            const filterButtons = document.querySelectorAll('.filter-chip');
            filterButtons.forEach(button => {
                button.addEventListener('click', () => {
                    currentPage = 1;
                    currentStatus = button.dataset.status;
                    updateFilterButtons(currentStatus);
                    refreshCurrentView();
                });
            });

            document.getElementById('hotelsTab').addEventListener('click', () => {
                currentPage = 1;
                currentTab = 'hotels';
                switchTab('hotels');
                refreshCurrentView();
                updateStatusCounts(hotels);
            });

            document.getElementById('roomsTab').addEventListener('click', () => {
                currentPage = 1;
                currentTab = 'rooms';
                switchTab('rooms');
                refreshCurrentView();
                updateStatusCounts(rooms);
            });
        }

        // CSS 樣式
        const style = document.createElement('style');
        style.textContent = `
            .pagination {
                display: flex;
                justify-content: center;
                align-items: center;
                margin: 20px 0;
                gap: 10px;
            }
            
            .page-btn {
                padding: 8px 16px;
                border: 1px solid #e2e8f0;
                background-color: white;
                border-radius: 4px;
                cursor: pointer;
                transition: all 0.2s;
            }
            
            .page-btn:hover:not([disabled]) {
                background-color: #e2e8f0;
            }
            
            .page-btn[disabled] {
                opacity: 0.5;
                cursor: not-allowed;
            }
            
            .page-info {
                padding: 8px 16px;
                background-color: #f7fafc;
                border-radius: 4px;
            }
            
            .cards-grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
                gap: 20px;
                padding: 20px;
                max-width: 1200px;
                margin: 0 auto;
            }
        `;
        document.head.appendChild(style);

        // 初始化
		hotels = await fetchHotels();
        rooms = await fetchRooms();
        setupPagination();
        renderHotelsList('all');
        renderRoomsList('all');
        updateStatusCounts(hotels);
        initializeFilters();
        updatePaginationState();
		
    } catch (error) {
        console.error('Error initializing review page:', error);
    }
});