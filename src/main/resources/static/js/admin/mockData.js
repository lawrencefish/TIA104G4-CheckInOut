// 模擬資料庫數據
const mockCoupons = {
    // 模擬資料存儲
    coupons: [
        {
            id: 1,
            name: "新會員優惠券",
            description: "新會員首次消費可享有85折優惠",
            redemptionCount: 150,
            minSpend: 1000,
            startDate: "2024-01-01",
            endDate: "2024-12-31"
        },
        {
            id: 2,
            name: "春節特惠券",
            description: "春節期間消費滿2000元現折200元",
            redemptionCount: 89,
            minSpend: 2000,
            startDate: "2024-02-01",
            endDate: "2024-02-29"
        },
        {
            id: 3,
            name: "週年慶優惠",
            description: "週年慶期間消費滿3000元現折500元",
            redemptionCount: 200,
            minSpend: 3000,
            startDate: "2024-03-01",
            endDate: "2024-03-31"
        },
        {
            id: 4,
            name: "生日優惠券",
            description: "生日當月消費可享9折優惠",
            redemptionCount: 75,
            minSpend: 500,
            startDate: "2024-01-01",
            endDate: "2024-12-31"
        },
        {
            id: 5,
            name: "暑期特惠",
            description: "暑假期間住宿享有85折優惠",
            redemptionCount: 120,
            minSpend: 2500,
            startDate: "2024-07-01",
            endDate: "2024-08-31"
        }
    ],
    
    // 模擬 API 方法
    getAllCoupons: function(page = 1, limit = 5) {
        const startIndex = (page - 1) * limit;
        const endIndex = startIndex + limit;
        const paginatedCoupons = this.coupons.slice(startIndex, endIndex);
        
        return {
            coupons: paginatedCoupons,
            totalPages: Math.ceil(this.coupons.length / limit),
            currentPage: page,
            totalItems: this.coupons.length
        };
    },

    searchCoupons: function(query) {
        const filteredCoupons = this.coupons.filter(coupon => 
            coupon.name.toLowerCase().includes(query.toLowerCase()) ||
            coupon.description.toLowerCase().includes(query.toLowerCase())
        );
        
        return {
            coupons: filteredCoupons,
            totalPages: 1,
            currentPage: 1,
            totalItems: filteredCoupons.length
        };
    },

    deleteCoupon: function(id) {
        const index = this.coupons.findIndex(coupon => coupon.id === id);
        if (index !== -1) {
            this.coupons.splice(index, 1);
            return true;
        }
        return false;
    },

    getCouponById: function(id) {
        return this.coupons.find(coupon => coupon.id === parseInt(id));
    }
};

// 修改 c3.js 中的 API 調用
function loadTableData(searchQuery = '') {
    let data;
    if (searchQuery) {
        data = mockCoupons.searchCoupons(searchQuery);
    } else {
        // 獲取當前頁碼，預設為1
        const urlParams = new URLSearchParams(window.location.search);
        const currentPage = parseInt(urlParams.get('page')) || 1;
        data = mockCoupons.getAllCoupons(currentPage);
    }
    
    renderTable(data);
    renderPagination(data.totalPages);
}

// 修改刪除功能
function deleteCoupon(id) {
    if (confirm("確定要刪除此優惠券嗎？")) {
        const success = mockCoupons.deleteCoupon(id);
        if (success) {
            loadTableData();
            alert('刪除成功！');
        } else {
            alert('刪除失敗，請稍後再試');
        }
    }
} 