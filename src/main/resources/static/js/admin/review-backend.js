document.addEventListener('DOMContentLoaded', async () => {
    try {
        // 載入 Header 和 Footer
        const headerResponse = await fetch('backend-header.html');
        const headerHtml = await headerResponse.text();
        document.getElementById('header').innerHTML = headerHtml;
        initializeHeader();

        const footerResponse = await fetch('backend-footer.html');
        const footerHtml = await footerResponse.text();
        document.getElementById('footer').innerHTML = footerHtml;

        // Mock 資料 - 添加狀態資訊
        const mockHotels = [
            {
                id: 1,
                name: '五星大飯店',
                address: '台北市信義區市府路45號',
                taxId: '12345678',
                image: '/img/環境照片.webp',
                status: 'pending',
                submitDate: '2024-03-15'
            },
            {
                id: 2,
                name: '豪華商旅',
                address: '台北市大安區敦化南路100號',
                taxId: '87654321',
                image: '/img/環境照片2.jpg',
                status: 'approved',
                submitDate: '2024-03-14'
            }
        ];

        const mockRoomTypes = [
            {
                id: 1,
                hotelId: 1,
                hotelName: '五星大飯店',
                image: '/img/雙人房.webp',
                roomTypeName: '豪華雙人房',
                roomCount: 20,
                status: 'pending',
                submitDate: '2024-03-15'
            },
            {
                id: 2,
                hotelId: 2,
                hotelName: '豪華商旅',
                image: '/img/單人房.webp',
                roomTypeName: '標準單人房',
                roomCount: 15,
                status: 'rejected',
                submitDate: '2024-03-13'
            }
        ];

        // 狀態轉換為中文
        const statusText = {
            pending: '待審核',
            approved: '已通過',
            rejected: '已拒絕'
        };

        // 渲染業者列表
        function renderHotelsList(status = 'all') {
            const filteredHotels = status === 'all' 
                ? mockHotels 
                : mockHotels.filter(hotel => hotel.status === status);

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
                            <span class="status-badge ${hotel.status}">${statusText[hotel.status]}</span>
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
                card.addEventListener('click', (e) => {
                    if (e.target.classList.contains('action-button')) {
                        const hotelId = card.getAttribute('data-hotel-id');
                        window.location.href = `/industry-review.html?id=${hotelId}`;
                    }
                });
            });
        }

        // 渲染房型列表
        function renderRoomsList(status = 'all') {
            const filteredRooms = status === 'all'
                ? mockRoomTypes
                : mockRoomTypes.filter(room => room.status === status);

            const roomsList = document.getElementById('roomsList');
            roomsList.innerHTML = filteredRooms.map(room => `
                <div class="review-card" data-room-id="${room.id}">
                    <img 
                        src="${room.image}" 
                        alt="${room.roomTypeName}" 
                        class="card-image"
                    />
                    <div class="card-content">
                        <div class="card-header">
                            <h3 class="card-title">${room.roomTypeName}</h3>
                            <span class="status-badge ${room.status}">${statusText[room.status]}</span>
                        </div>
                        <p class="card-info">飯店：${room.hotelName}</p>
                        <p class="card-info">房間數量：${room.roomCount}</p>
                        <p class="card-info">提交日期：${room.submitDate}</p>
                        <div class="card-actions">
                            <button class="action-button primary-button">審核詳情</button>
                        </div>
                    </div>
                </div>
            `).join('');

            // 添加點擊事件
            document.querySelectorAll('.review-card').forEach(card => {
                card.addEventListener('click', (e) => {
                    if (e.target.classList.contains('action-button')) {
                        const roomId = card.getAttribute('data-room-id');
                        window.location.href = `/roomtype-review.html?id=${roomId}`;
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
                pending: items.filter(item => item.status === 'pending').length,
                approved: items.filter(item => item.status === 'approved').length,
                rejected: items.filter(item => item.status === 'rejected').length
            };

            // 更新數量顯示
            document.querySelector('[data-status="all"] .count-badge').textContent = counts.all;
            document.querySelector('[data-status="pending"] .count-badge').textContent = counts.pending;
            document.querySelector('[data-status="approved"] .count-badge').textContent = counts.approved;
            document.querySelector('[data-status="rejected"] .count-badge').textContent = counts.rejected;
        }

        // 初始化狀態篩選功能
        function initializeFilters() {
            const filterButtons = document.querySelectorAll('.filter-chip');
            let currentTab = 'hotels';
            let currentStatus = 'all';

            // 初始化狀態數量
            updateStatusCounts(mockHotels);

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
                updateStatusCounts(mockHotels);
            });

            document.getElementById('roomsTab').addEventListener('click', () => {
                currentTab = 'rooms';
                switchTab('rooms');
                renderRoomsList(currentStatus);
                updateStatusCounts(mockRoomTypes);
            });

            // 初始化顯示
            updateFilterButtons('all');
            renderHotelsList('all');
        }

        // 初始化所有功能
        initializeFilters();

    } catch (error) {
        console.error('Error initializing review page:', error);
    }
});