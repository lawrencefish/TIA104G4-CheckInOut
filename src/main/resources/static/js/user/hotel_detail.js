// 當文件載入完成後執行初始化
$(document).ready(function() {
    const today = new Date();

    // 初始化日曆，顯示當前月份
    generateCalendar('#calendar-wrapper', today.getFullYear(), today.getMonth());

    // 點擊日期範圍顯示區域時切換日曆的顯示狀態
    $('#date-range').on('click', function(e) {
        e.stopPropagation();
        const $calendar = $('#calendar-wrapper');
        $calendar.toggleClass('d-none');
    });

    // 點擊搜尋按鈕時的處理
    $('#search-button').on('click', function() {
        if (startDate && endDate) {
            alert(`搜尋條件: 入住 ${startDate}, 退房 ${endDate}`);
        } else {
            alert('請選擇完整的入住與退房日期！');
        }
    });

    // 點擊日曆和日期範圍顯示區域以外的地方時關閉日曆
    $(document).on('click', function(e) {
        if (!$(e.target).closest('#calendar-wrapper, #date-range').length) {
            $('#calendar-wrapper').addClass('d-none');
        }
    });
});