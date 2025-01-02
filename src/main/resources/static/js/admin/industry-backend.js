// 全局狀態管理
const state = {
  currentSlide: 0,
  slides: [],
  currentReviewPage: 1,
  reviewsPerPage: 5,
  allReviews: [],
  businessData: null
};

// API 端點配置（暫時註解）
/*
const API_ENDPOINTS = {
  BUSINESS_INFO: '/api/business',
  REVIEWS: '/api/reviews',
  IMAGES: '/api/images'
};
*/

// 初始化函數
async function initializeApp() {
  try {
    console.log('初始化應用程式開始');
    
    // 從 URL 獲取業者 ID
    const urlParams = new URLSearchParams(window.location.search);
    const businessId = urlParams.get('id');
    
    if (!businessId) {
      console.warn('未提供業者ID，使用預設值');
      // throw new Error('未提供業者ID');
    }

    // 載入業者數據
    await loadBusinessData(businessId);
    
    // 初始化組件
    initializeComponents();
    
    // 設置事件監聽器
    setupEventListeners();
    
    // 載入預設評論
    changeReview('member');
    
    console.log('初始化應用程式完成');
  } catch (error) {
    console.error('初始化錯誤:', error);
    showErrorMessage('載入頁面時發生錯誤');
  }
}

// 載入業者數據
async function loadBusinessData(businessId) {
  try {
    console.log('開始載入業者資料，ID:', businessId);
    
    // API 調用（暫時註解）
    /*
    const response = await fetch(`${API_ENDPOINTS.BUSINESS_INFO}/${businessId}`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    state.businessData = await response.json();
    */
    
    // 使用假資料
    state.businessData = {
      name: '好吃好睡飯店',
      id: '12345678',
      city: '台北市',
      area: '大安區',
      address: '台北市大安區光復南路123號',
      phone: '1234-5678',
      email: 'example@example.com',
      images: {
        idCardFront: './img/假身分證正面.png',
        idCardBack: './img/假身分證正面.png',
        businessLicense: './img/營業證樣本.png',
        environment: [
          './img/環境照片.webp',
          './img/環境照片2.jpg',
          './img/環境照片3.jpg'
        ]
      }
    };

    console.log('業者資料載入成功:', state.businessData);
    await updateBusinessInfo();
    
  } catch (error) {
    console.error('載入業者資料時發生錯誤:', error);
    throw error;
  }
}

// 更新業者信息
async function updateBusinessInfo() {
  try {
    // 更新基本資訊
    const fieldMappings = {
      'company-name': 'name',
      'company-id': 'id',
      'company-city': 'city',
      'company-area': 'area',
      'company-address': 'address',
      'company-phone': 'phone',
      'company-email': 'email'
    };

    Object.entries(fieldMappings).forEach(([elementId, dataField]) => {
      const element = document.querySelector(`#${elementId} .content`);
      if (element && state.businessData[dataField]) {
        element.textContent = state.businessData[dataField];
        console.log(`更新 ${elementId} 為: ${state.businessData[dataField]}`);
      }
    });

    // 更新圖片
    await updateImages();
  } catch (error) {
    console.error('更新業者資訊時發生錯誤:', error);
    throw error;
  }
}

// 更新圖片
async function updateImages() {
  try {
    // API 調用（暫時註解）
    /*
    const response = await fetch(`${API_ENDPOINTS.IMAGES}/${state.businessData.id}`);
    const images = await response.json();
    */

    const images = state.businessData.images;

    // 更新證件圖片
    document.getElementById('image1').src = images.idCardFront;
    document.getElementById('image2').src = images.idCardBack;
    document.getElementById('image3').src = images.businessLicense;

    // 更新輪播圖片
    const carouselContent = document.querySelector('.carousel-content');
    if (carouselContent) {
      carouselContent.innerHTML = images.environment
        .map((src, index) => `
          <img src="${src}" 
               alt="環境照片 ${index + 1}" 
               class="carousel-image" 
               onclick="openModal(this)" 
               style="display: ${index === 0 ? 'block' : 'none'}">`)
        .join('');
      
      state.slides = document.getElementsByClassName('carousel-image');
    }
  } catch (error) {
    console.error('更新圖片時發生錯誤:', error);
    throw error;
  }
}

// 評論功能
async function changeReview(type) {
  console.log('切換評論類型:', type);
  
  try {
    const buttons = document.querySelectorAll('.reviewBtn');
    buttons.forEach(button => {
      if (button.getAttribute('data-review-type') === type) {
        button.classList.add('active');
      } else {
        button.classList.remove('active');
      }
    });

    const activeButton = document.querySelector(`[data-review-type="${type}"]`);
    if (activeButton) {
      activeButton.classList.add('active');
    }

    // API 調用（暫時註解）
    /*
    const response = await fetch(`${API_ENDPOINTS.REVIEWS}/${type}`);
    state.allReviews = await response.json();
    */

    // 使用假評論資料
    const mockReviews = {
      member: [
        { title: '會員評論 A', description: '這是會員評論 A 的詳細描述。' },
        { title: '會員評論 B', description: '這是會員評論 B 的詳細描述。' },
        { title: '會員評論 C', description: '這是會員評論 C 的詳細描述。' },
        { title: '會員評論 D', description: '這是會員評論 D 的詳細描述。' },
        { title: '會員評論 E', description: '這是會員評論 E 的詳細描述。' }
      ],
      user: [
        { title: '來自會員的評論 A', description: '這是來自會員的評論 A 的詳細描述。' },
        { title: '來自會員的評論 B', description: '這是來自會員的評論 B 的詳細描述。' },
        { title: '來自會員的評論 C', description: '這是來自會員的評論 C 的詳細描述。' },
        { title: '來自會員的評論 D', description: '這是來自會員的評論 D 的詳細描述。' },
        { title: '來自會員的評論 D', description: '這是來自會員的評論 D 的詳細描述。' }
      ]
    };

    state.allReviews = mockReviews[type] || [];
    state.currentReviewPage = 1;
    displayReviews();
  } catch (error) {
    console.error('載入評論時發生錯誤:', error);
    showErrorMessage('載入評論時發生錯誤');
  }
}

// 顯示評論
function displayReviews() {
  const reviewList = document.querySelector('.review-list');
  if (!reviewList) return;

  reviewList.innerHTML = '';

  const startIndex = (state.currentReviewPage - 1) * state.reviewsPerPage;
  const endIndex = startIndex + state.reviewsPerPage;
  const reviewsToShow = state.allReviews.slice(startIndex, endIndex);

  reviewsToShow.forEach(review => {
    const reviewCard = document.createElement('div');
    reviewCard.className = 'review-card';
    reviewCard.innerHTML = `
      <p>會員名稱：${review.title}</p>
      <p class="description">${review.description}</p>
    `;
    reviewList.appendChild(reviewCard);
  });

// 更新分頁按鈕顯示
// 更新分頁按鈕狀態
const loadMoreBtn = document.getElementById('loadMoreBtn');
const backBtn = document.getElementById('backBtn');

if (loadMoreBtn) {
  loadMoreBtn.style.display = endIndex < state.allReviews.length ? 'block' : 'none';
}

if (backBtn) {
  backBtn.style.display = state.currentReviewPage > 1 ? 'block' : 'none';
}
}

// 輪播功能
function changeSlide(direction) {
  if (state.slides.length === 0) return;

  state.slides[state.currentSlide].style.display = 'none';
  state.currentSlide = (state.currentSlide + direction + state.slides.length) % state.slides.length;
  state.slides[state.currentSlide].style.display = 'block';
}

function startAutoSlide() {
  setInterval(() => changeSlide(1), 5000);
}

// 圖片模態框功能
function openModal(img) {
  if (!img?.src) {
    console.error('無效的圖片來源');
    return;
  }

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

// 加載更多評論
function loadMoreReviews() {
  state.currentReviewPage++;
  displayReviews();
}

// 返回上一頁
function goBack() {
  if (state.currentReviewPage > 1) {
    state.currentReviewPage--;
    displayReviews();
  }
}

// 顯示錯誤訊息
function showErrorMessage(message) {
  console.error(message);
  const errorElement = document.getElementById('errorMessage');
  if (errorElement) {
    errorElement.textContent = message;
    errorElement.style.display = 'block';
    setTimeout(() => {
      errorElement.style.display = 'none';
    }, 3000);
  } else {
    alert(message);
  }
}

// 初始化組件
function initializeComponents() {
  state.slides = document.getElementsByClassName('carousel-image');
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

  // 評論按鈕點擊事件
  const reviewButtons = document.querySelectorAll('.reviewBtn');
  reviewButtons.forEach(button => {
    button.addEventListener('click', () => {
      const type = button.getAttribute('data-review-type');
      changeReview(type);
    });
  });

  // 開始自動輪播
  startAutoSlide();
}

// 當 DOM 載入完成時初始化應用
document.addEventListener('DOMContentLoaded', initializeApp);