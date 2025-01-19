// 全局狀態管理
const state = {
	map: null,
    geocoder: null,
    mapInitialized: false,
	currentSlide: 0  // 添加當前幻燈片索引
};

// 設置 API 端點
const API_ENDPOINTS = {
//    HOTEL_INFO: '/business/hotelInfo',
    HOTEL_IMAGES: '/adminHotel/getHotelImages'
};

// 將 initMap 設為全域函數
window.initMap = function() {
    console.log('Google Maps API 已載入');
    initializeMap();
};

// 初始化地圖
function initializeMap() {
    if (!window.google || !window.google.maps) {
        console.error('Google Maps API 未載入');
        return;
    }

    try {
        // 從 HTML 中獲取地址資料
        const cityElement = document.querySelector('#company-city .content');
        const districtElement = document.querySelector('#company-area .content');
        const addressElement = document.querySelector('#company-address .content');
        const companyNameElement = document.querySelector('#company-name .content');

        if (!cityElement || !districtElement || !addressElement) {
            console.error('找不到必要的地址資料');
            return;
        }

        const city = cityElement.textContent.trim();
        const district = districtElement.textContent.trim();
        const address = addressElement.textContent.trim();
        const companyName = companyNameElement ? companyNameElement.textContent.trim() : '';

        const fullAddress = `${city}${district}${address}`;
        
        const mapElement = document.getElementById('map');
        if (!mapElement) {
            console.error('找不到地圖容器元素');
            return;
        }

        // 清空地圖容器
        mapElement.innerHTML = '';
        
        // 建立地圖
        state.map = new google.maps.Map(mapElement, {
            zoom: 16,
            center: { lat: 25.033964, lng: 121.564468 }, // 預設台北市中心
            mapTypeControl: false,
            streetViewControl: false
        });

        // 初始化地理編碼器
        state.geocoder = new google.maps.Geocoder();

        // 地理編碼
        state.geocoder.geocode({ address: fullAddress }, (results, status) => {
            if (status === "OK" && results[0]) {
                const location = results[0].geometry.location;
                state.map.setCenter(location);
                new google.maps.Marker({
                    map: state.map,
                    position: location,
                    title: companyName
                });
            } else {
                console.error("地理編碼失敗: ", status);
                showErrorMessage("無法在地圖上顯示地址");
            }
        });
    } catch (error) {
        console.error('初始化地圖時發生錯誤:', error);
        showErrorMessage('地圖載入失敗');
    }
}

// 錯誤訊息顯示函數
function showErrorMessage(message) {
    const errorElement = document.getElementById('errorMessage');
    if (errorElement) {
        errorElement.textContent = message;
        errorElement.style.display = 'block';
        setTimeout(() => {
            errorElement.style.display = 'none';
        }, 3000);
    } else {
        console.error(message);
    }
}

// 載入環境照片
async function loadEnvironmentPhotos() {
	try {
        // 從 URL 獲取 hotelId
        const urlParams = new URLSearchParams(window.location.search);
        const hotelId = urlParams.get('id');
        
        if (!hotelId) {
            console.error('找不到飯店 ID');
            return;
        }

        // 使用新的 API 端點
        const response = await fetch(`${API_ENDPOINTS.HOTEL_IMAGES}/${hotelId}`);
        if (!response.ok) {
            throw new Error('Failed to load environment photos');
        }
        
        const imageIds = await response.json();
        if (imageIds && imageIds.length > 0) {
            updateEnvironmentPhotos(imageIds);
        }
    } catch (error) {
        console.error('載入環境照片失敗:', error);
        showErrorMessage('載入環境照片失敗');
    }
}

// 更新環境照片
function updateEnvironmentPhotos(imageIds) {
    const carouselContent = document.querySelector('.carousel-content');
    if (!carouselContent || !imageIds.length) return;

    carouselContent.innerHTML = imageIds
        .map((imageId, index) => `
			<div class="carousel-item ${index === 0 ? 'active' : ''}">
                <img src="/adminHotel/getHotelImage/${imageId}" 
                     alt="環境照片 ${index + 1}" 
                     class="carousel-image" 
                     onclick="openModal(this)">
            </div>
        `).join('');
    
		// 初始化輪播狀態
	    state.currentSlide = 0;
	    
	    // 如果有多張圖片，啟動自動輪播
	    if (imageIds.length > 1) {
	        startAutoSlide();
	    }
	}

// 輪播功能
function changeSlide(direction) {
	const slides = document.getElementsByClassName('carousel-item');
	    if (!slides.length) return;
	    
	    // 移除當前活動幻燈片的 active 類
	    slides[state.currentSlide].classList.remove('active');
	    
	    // 計算下一張幻燈片的索引
	    state.currentSlide = (state.currentSlide + direction + slides.length) % slides.length;
	    
	    // 添加 active 類到新的活動幻燈片
	    slides[state.currentSlide].classList.add('active');
	}
	
function startAutoSlide() {
	const slides = document.getElementsByClassName('carousel-item');
    if (slides.length > 1) {  // 只有當有多張圖片時才啟動自動輪播
        setInterval(() => changeSlide(1), 3000);
    }
}
// 圖片模態框功能
function openModal(img) {
    if (!img?.src) return;
    
    const modal = document.getElementById('imageModal');
    const modalImage = document.getElementById('modalImage');
    if (modal && modalImage) {
        modalImage.src = img.src;
        modal.style.display = 'flex';
    }
}

function closeModal() {
    const modal = document.getElementById('imageModal');
    if (modal) {
        modal.style.display = 'none';
    }
}


// 初始化組件
function initializeComponents() {
	loadEnvironmentPhotos(); // 在初始化時載入照片
    state.slides = document.getElementsByClassName('carousel-item');
}

// 設置事件監聽器
function setupEventListeners() {
    // 模態框點擊關閉
    window.onclick = event => {
        const modal = document.getElementById('imageModal');
        if (event.target === modal) {
            closeModal();
        }
    };
}

// 當 DOM 載入完成時初始化應用
document.addEventListener('DOMContentLoaded', () => {
	initializeComponents();
    setupEventListeners();
	
	// 如果 Google Maps API 已經載入，直接初始化地圖
    if (window.google && window.google.maps) {
        initializeMap();
    }
});
