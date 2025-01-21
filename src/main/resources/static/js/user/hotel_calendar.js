// 全域變數宣告
let startDate = null;  // 儲存入住日期
let endDate = null;    // 儲存退房日期
const MONTH_NAMES = ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"];

// 可禁用的日期列表
let disableDate = [];
let dateArray = [];

// 初始化日曆，顯示當前月份
 $('#date-range').on('click',function(e){
    fetchCalendarInfo()
        .then(function (data) {
            const today = new Date();
            console.log(data)
            dateArray = data.date;
            // 等資料加載完後再生成日曆
            disableDate = [];
            generateCalendar('#calendar-wrapper', today.getFullYear(), today.getMonth());
        })
        .catch(function (error) {
            console.error("Error fetching inventory:", error);
        });
})

/**
 * 檢查是否為閏年
 * @param {number} year - 要檢查的年份
 * @returns {boolean} - 是否為閏年
 */
function isLeapYear(year) {
    return (year % 4 === 0 && year % 100 !== 0) || year % 400 === 0;
}

/**
 * 取得指定月份的天數
 * @param {number} month - 月份（0-11）
 * @param {number} year - 年份
 * @returns {number} - 該月份的總天數
 */
function daysInMonth(month, year) {
    return [31, isLeapYear(year) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month];
}

/**
 * 產生日曆 HTML 並處理相關事件
 * @param {string} containerId - 容器的 ID
 * @param {number} year - 年份
 * @param {number} month - 月份（0-11）
 */
function generateCalendar(containerId, year, month) {
    const $container = $(containerId);
    $container.empty();  // 清空容器內容

    // 計算容器的寬度
    const containerWidth = $container.width();

    // 動態調整表格寬度（每個單元格等分）
    const cellWidth = Math.floor(containerWidth / 7);

    // 產生日曆標題列（年月和切換按鈕）
    let calendarHtml = `
        <div class="calendar-header d-flex justify-content-between mb-3">
            <button class="btn btn-sm btn-outline-secondary prev-month">&lt;</button>
            <strong>${year} 年 ${MONTH_NAMES[month]}</strong>
            <button class="btn btn-sm btn-outline-secondary next-month">&gt;</button>
            <button type="button" class="btn btn-sm btn-outline-danger clear-dates">清除日期</button>
        </div>
    `;

    // 產生星期標題列
    calendarHtml += '<table class="table table-bordered" style="table-layout: fixed; width: 100%;">';
    calendarHtml += '<thead><tr>';
    ['日', '一', '二', '三', '四', '五', '六'].forEach(day => {
        calendarHtml += `<th class="text-center" style="width: ${cellWidth}px;">${day}</th>`;
    });
    calendarHtml += '</tr></thead><tbody>';

    // 計算當月第一天是星期幾
    const firstDay = new Date(year, month, 1).getDay();
    const totalDays = daysInMonth(month, year);

    // 產生日期格子
    let day = 1;
    // 可禁用的日期列表
    for (let i = 0; i < 6; i++) {
        calendarHtml += '<tr>';
        for (let j = 0; j < 7; j++) {
            if (i === 0 && j < firstDay || day > totalDays) {
                // 空白格子（上個月或下個月的日期）
                calendarHtml += `<td class="text-muted" style="width: ${cellWidth}px;"></td>`;
            } else {
                const dateString = formatDate(new Date(year, month, day));
                const date = new Date(dateString);
                const today = new Date();    
                date.setHours(0,0,0,0);
                today.setHours(0, 0, 0, 0);
                // 找到與 dateString 匹配的項目
                const matchedItem = dateArray.find(item => item.date === dateString);
                let price = "";
                if (matchedItem && (today.getTime() <= date.getTime())){
                    price =  matchedItem.price ? "$"+matchedItem.price : "";
                }
                // 判斷是否禁用
                let disable = !matchedItem || parseInt(matchedItem.count) <= 0 ? dateString : null;
                if (disable) {
                    disableDate.push(disable); // 將禁用的日期加入 disableDate
                    price = " ";
                }
                const isDisabled = new Date(dateString) < today || disableDate.includes(dateString);
                // 日期格子內容（含日期和價格）
                calendarHtml += `
                    <td data-date="${dateString}" 
                        class="calendar-cell text-center ${isDisabled ? 'disabled bg-secondary bg-opacity-10' : ''}" 
                        style="cursor: ${isDisabled ? 'not-allowed' : 'pointer'}; width: ${cellWidth}px;">
                        <div class="date-content">
                            <div class="date-number">${day}</div>
                            <div class="info-display">${price}</div>
                        </div>
                    </td>
                `;
                day++;
            }
        }
        calendarHtml += '</tr>';
        if (day > totalDays) break;  // 當月天數填完就結束
    }
    calendarHtml += '</tbody></table>';

    $container.html(calendarHtml);

    // 綁定上個月按鈕點擊事件
    $container.find('.prev-month').on('click', function (e) {
        e.stopPropagation();
        const newMonth = month === 0 ? 11 : month - 1;
        const newYear = month === 0 ? year - 1 : year;
        generateCalendar(containerId, newYear, newMonth);
        highlightDateRange();
    });

    // 綁定下個月按鈕點擊事件
    $container.find('.next-month').on('click', function (e) {
        e.stopPropagation();
        const newMonth = month === 11 ? 0 : month + 1;
        const newYear = month === 11 ? year + 1 : year;
        generateCalendar(containerId, newYear, newMonth);
        highlightDateRange();
    });

    // 綁定清除日期按鈕點擊事件
    $container.find('.clear-dates').on('click', function (e) {
        e.stopPropagation();
        clearSelectedDates();
    });

    // 綁定日期選擇事件（排除已停用的日期）
    $container.find('.calendar-cell:not(.disabled)').on('click', function (e) {
        e.stopPropagation();
        const selectedDate = $(this).data('date');
        handleDateSelection(selectedDate);
    });

}

/**
 * 處理日期選擇邏輯
 * @param {string} dateString - 選擇的日期字串（YYYY-MM-DD 格式）
 */
function handleDateSelection(dateString) {
    const selectedDate = new Date(dateString);

    if (!startDate || (startDate && endDate)) {
        // 開始新的選擇範圍
        startDate = dateString;
        endDate = null;
    } else {
        // 選擇結束日期
        const startDateTime = new Date(startDate);

        // 確保開始日期在結束日期之前
        if (selectedDate < startDateTime) {
            endDate = startDate;
            startDate = dateString;
        } else {
            endDate = dateString;
        }

        // 驗證日期範圍是否符合規則
        const daysDiff = Math.ceil((new Date(endDate) - new Date(startDate)) / (1000 * 60 * 60 * 24));

        if (daysDiff < 1) {
            showModal('請選擇至少2日的範圍！');
            clearSelectedDates();
            return;
        }
        if (daysDiff > 15) {
            showModal('請勿選擇超過15日的範圍！');
            clearSelectedDates();
            return;
        }

        // 驗證是否有禁用日期出現在範圍內
        const rangeContainsDisabled = disableDate.some(disabledDate => {
            const disabledDateTime = new Date(disabledDate).getTime();
            return disabledDateTime >= new Date(startDate).getTime() && disabledDateTime <= new Date(endDate).getTime();
        });

        if (rangeContainsDisabled) {
            showModal('選擇的日期範圍包含不可用的日期，請重新選擇！');
            clearSelectedDates();
            return;
        }
    }

    highlightDateRange();  // 更新日期範圍的視覺效果
    updateDateRangeDisplay();  // 更新日期範圍的顯示文字
}

/**
 * 清除所選日期範圍
 */
function clearSelectedDates() {
    startDate = null;
    endDate = null;
    highlightDateRange();  // 清除範圍的視覺效果
    updateDateRangeDisplay();  // 清空顯示文字
}

/**
 * 更新日期範圍的視覺效果
 */
function highlightDateRange() {
    $('.calendar-cell').removeClass('bg-primary text-white bg-info');

    if (!startDate) return;

    $('.calendar-cell').each(function () {
        const cellDate = $(this).data('date');
        if (!cellDate) return;

        if (cellDate === startDate) {
            $(this).addClass('bg-primary text-white');  // 入住日期突顯
        } else if (endDate && cellDate >= startDate && cellDate <= endDate) {
            $(this).addClass('bg-info');  // 選擇範圍內的日期標記
        }
    });
}

/**
 * 更新日期範圍的顯示文字
 */
function updateDateRangeDisplay() {
    let html;

    if (startDate) {
        if (endDate) {
            html = `入住: <span id="start_date">${startDate}</span> - 退房: <span id="end_date">${endDate}</span>`;
        } else {
            html = `入住: <span id="start_date">${startDate}</span>`;
        }
    } else {
        html = '選擇日期';
    }
    $('#date-range').html(html);
}

/**
 * 格式化日期為 YYYY-MM-DD 格式
 * @param {Date} date - 日期物件
 * @returns {string} - 格式化後的日期字串
 */
function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

//取得庫存值
function fetchCalendarInfo() {
    return $.ajax({
        url: '/booking/api/calendar/inventory',
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify({
            id: hotelId,
            roomNum: $('#room_num').val() == "" ? 1 : $('#room_num').val(),
            guestNum: $('#people_num').val() == "" ? 1 : $('#people_num').val()
        })
    });
}
