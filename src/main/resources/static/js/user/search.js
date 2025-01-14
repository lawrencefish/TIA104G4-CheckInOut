const apikey = "AIzaSyAQ4SS_rzxn4J8dPktZjUiVMAkjGA_dCuo";

// 初始化 Google Maps JavaScript API
(g => {
    var h, a, k, p = "The Google Maps JavaScript API", c = "google", l = "importLibrary", q = "__ib__", m = document, b = window;
    b = b[c] || (b[c] = {});
    var d = b.maps || (b.maps = {}), r = new Set, e = new URLSearchParams,
        u = () => h || (h = new Promise(async (f, n) => {
            // 建立 script 元素，用來載入 Google Maps API
            await (a = m.createElement("script"));
            // 設定 API 的參數
            e.set("libraries", [...r] + "");
            for (k in g) e.set(k.replace(/[A-Z]/g, t => "_" + t[0].toLowerCase()), g[k]);
            e.set("callback", c + ".maps." + q);
            a.src = `https://maps.${c}apis.com/maps/api/js?` + e;
            d[q] = f;
            // 當 API 載入失敗時，回傳錯誤
            a.onerror = () => h = n(Error(p + " could not load."));
            a.nonce = m.querySelector("script[nonce]")?.nonce || "";
            m.head.append(a); // 將 script 元素插入到 <head>
        }));
    d[l] ? console.warn(p + " only loads once. Ignoring:", g) : d[l] = (f, ...n) => r.add(f) && u().then(() => d[l](f, ...n));
})
    ({ key: apikey, v: "weekly", libraries: "places" });

// 定義地圖和其他全域變數
let map; // 儲存地圖物件
let currentCenter; // 儲存目前地圖的中心位置
let markers = []; // 儲存地圖上的標記
let currentData; // 全域變數，儲存最新的搜尋資料

$(document).ready(function () {
    getSearchResult()
        .then(function (data) {
            console.log(data);
            currentData = data; // 儲存初始取得的資料
            sortByDistance(data, data.myLat, data.myLnt);
            initMap(data);
        })
        .catch(function (error) {
            console.error(error);
            console.log(error.error);

        });
});

// 初始化地圖的主函式
async function initMap(data) {
    const { Map } = await google.maps.importLibrary("maps"); // 載入地圖庫
    const myLatlng = { lat: data.myLat, lng: data.myLnt }; // 設定初始的地圖中心點
    const { AdvancedMarkerElement } = await google.maps.importLibrary("marker"); // 載入標記庫

    findRoomWithLowestTotalPrice(data);
    // 初始化地圖並設定相關屬性
    map = new Map(document.getElementById("map"), {
        zoom: 14,
        center: myLatlng,
        mapId: "220d1160cfba5a2e",
        disableDefaultUI: true,
    });

    currentCenter = myLatlng;
    hotelLoading(data, map);

    // 搜尋列設定
    const input = document.getElementById("pac-input");
    const options = {
        componentRestrictions: { country: "tw" }  // 限制在台灣
    };

    // 使用 Autocomplete 而非 SearchBox
    const autocomplete = new google.maps.places.Autocomplete(input, options);

    // 將 Autocomplete 綁定至地圖範圍
    autocomplete.bindTo('bounds', map);

    // 將輸入框加入地圖控制項
    map.controls[google.maps.ControlPosition.TOP_RIGHT].push(input);

    // 監聽 Autocomplete 的地點變更事件
    autocomplete.addListener("place_changed", () => {
        const place = autocomplete.getPlace();
        if (!place.geometry) {
            console.log("找不到該地點的詳細資料");
            return;
        }

        // 如果有 viewport，使用它來調整地圖範圍；否則使用地點位置
        if (place.geometry.viewport) {
            map.fitBounds(place.geometry.viewport);
        } else {
            map.setCenter(place.geometry.location);
            map.setZoom(14); // 根據需要設定適當的縮放層級
        }
    });

    // 當地圖閒置（idle）時，重新載入飯店資訊
    map.addListener("idle", async () => {
        const newCenter = map.getCenter();
        const centerLatLng = {
            lat: newCenter.lat(),
            lng: newCenter.lng(),
        };
        if (
            !currentCenter ||
            currentCenter.lat !== centerLatLng.lat ||
            currentCenter.lng !== centerLatLng.lng
        ) {
            currentCenter = centerLatLng;

            try {
                await updateSearchResult(currentCenter);
                const searchData = await getSearchResult();
                currentData = searchData; // 更新全域變數為最新資料
                findRoomWithLowestTotalPrice(searchData);
                sortByDistance(currentData, currentData.myLat, currentData.myLnt);
                clearMarkers();
                hotelLoading(searchData, map);
                document.querySelector('.card-scroll').scrollTo({
                    top: 0,
                    behavior: 'smooth'
                  });                  
            } catch (error) {
                console.error("獲取資料時發生錯誤:", error);
            }
        }
    });

    // 事件監聽器使用全域 currentData
    $('#lowPriceFirst').on('click', (e) => {
        e.preventDefault();
        if (currentData) {
            sortByPrice(currentData, 'asc');
            hotelLoading(currentData, map);
        }
    });
    $('#highPriceFirst').on('click', (e) => {
        e.preventDefault();
        if (currentData) {
            sortByPrice(currentData, 'desc');
            hotelLoading(currentData, map);
        }
    });
    $('#nearestFirst').on('click', (e) => {
        e.preventDefault();
        if (currentData) {
            sortByDistance(currentData, currentData.myLat, currentData.myLnt);
            hotelLoading(currentData, map);
        }
    });
}


function addCardClickEvent(hotelID, position) {
    const targetCard = document.getElementById(`card-${hotelID}`);
    if (targetCard) {
        targetCard.addEventListener("click", () => {
            map.setZoom(14); // 設定地圖縮放層級
            map.panTo(position); // 將地圖移動到指定位置
        });
    }
}


// 載入飯店資訊並顯示在地圖上
// 修改後的 hotelLoading 函式，加入氣泡點擊事件
function hotelLoading(data, map) {
    // 呼叫 Google Maps Places API 並處理回應資料
    console.log(`Hotels reloaded:`);
    $(".card-scroll").empty(); // 清空舊的飯店清單
    // 處理每個飯店的資料
    data.hotels.forEach((d) => {
        let hotelName = d.hotel;
        let hotelReview = (d.ratings == 0 || d.ratings == null) ? "無法顯示分數" : d.ratings+"分";
        let hotelReviewConut = (d.comments == 0 || d.comments == null) ? "無法顯示評論" : d.comments+"則評論";
        let hotelCity = d.city + d.district;
        let hotelPrice = d.lowestTotalPrice; // 預設價格
        let hotelID = d.hotelID; // 飯店 ID
        let hotelIMG = "/booking/api/image/hotel/" + d.hotelID;
        let position = { lat: d.lat, lng: d.lng }; // 標記的位置
        // 建立飯店卡片
        let hotelCard = `
                    <div class="col ${hotelID}">
                        <div class="card h-100 my-3 position-relative" id="card-${hotelID}">
                            <!-- 愛心按鈕 -->
                            <button class="btn btn-light heart-button position-absolute top-0 end-0 m-2">
                                ♥
                            </button>
                            <div class="row g-0">
                                <div class="col-4" style="height:200px; overflow: hidden;">
                                    <img src="${hotelIMG}" style="width:100%; height:100%; object-fit:cover;" alt="${hotelName}">
                                </div>
                                <div class="col-8">
                                    <div class="card-body">
                                        <h5 class="card-title mb-1">${hotelName}</h5>
                                        <div class="card-text mb-4">
                                            <span class="badge bg-success">${hotelCity}</span>
                                            <span class="badge bg-primary"><span>${hotelReview}</span></span>
                                            <span class="badge bg-secondary"><span>${hotelReviewConut}</span></span>
                                        </div>
                                        <div class="card-footer bg-body border-0 p-0">
                                            <p class="hotel-price h4">NT$<span class="price">${hotelPrice}</span></p>
                                            <a class="btn btn-primary" href="/user/hotel_detail/${hotelID}">立刻訂房</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    `;
        $('.card-scroll').append(hotelCard);

        // 創建氣泡內容
        const markerContent = document.createElement("div");
        markerContent.innerHTML = `
                  <div class="bubble ${hotelID}">
                    <div class="bubble-content">
                      ${hotelName}
                    </div>
                    <div class="bubble-arrow"></div>
                  </div>
                `;

        // 標記的樣式
        const bubbleStyle = document.createElement("style");
        bubbleStyle.textContent = `
                  .bubble {
                    z-index: 999;
                    position: relative;
                    display: inline-block;
                    background-color: white;
                    border: 1px solid #ccc;
                    border-radius: 8px;
                    padding: 8px 12px;
                    font-size: 12px;
                    color: black;
                    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3);
                  }
                  .bubble-content {
                    text-align: center;
                  }
                  .bubble-arrow {
                    position: absolute;
                    bottom: -6px;
                    left: 50%;
                    transform: translateX(-50%);
                    width: 0;
                    height: 0;
                    border-style: solid;
                    border-width: 6px 6px 0 6px;
                    border-color: white transparent transparent transparent;
                  }
                  .bubble {
                    max-width: 150px; /* 限制氣泡寬度 */
                  }
                `;
        document.head.appendChild(bubbleStyle);

        // 創建地圖上的標記
        const marker = new google.maps.marker.AdvancedMarkerElement({
            map,
            position: position,
            content: markerContent,
            gmpClickable: true,
        });

        // 添加點擊氣泡的事件，跳轉到對應的卡片
        markerContent.parentElement?.parentElement?.addEventListener("click", (e) => {
            const targetCard = document.getElementById(`card-${hotelID}`);
            if (targetCard) {
                // 滾動到對應卡片
                targetCard.scrollIntoView({ behavior: "smooth", block: "center" });
                map.setZoom(16); // 設定地圖縮放層級
                map.panTo(position); // 將地圖移動到指定位置            
            }
        });
        addCardClickEvent(hotelID, position);
        markers.push(marker); // 儲存標記
    });
};

function findRoomWithLowestTotalPrice(data) {
    // 遍歷每一家飯店
    data.hotels.forEach(hotel => {
        let minPriceSum = Infinity;
        let cheapestRoom = null;

        // 對該飯店的每個房型進行計算
        hotel.rooms.forEach(room => {
            // 計算該房型所有日期的總價格
            const totalPrice = room.dates.reduce((sum, date) => sum + date.price, 0);

            // 更新最小價格與最便宜房型
            if (totalPrice < minPriceSum) {
                minPriceSum = totalPrice;
                cheapestRoom = room;
            }
        });

        // 將最便宜房型及價格存入飯店物件中
        hotel.cheapestRoom = cheapestRoom;
        hotel.lowestTotalPrice = minPriceSum;
    });

    // 此函式不需要回傳值，因為它直接修改了 data 物件中的資料
}


// 清除地圖上的標記
function clearMarkers() {
    markers.forEach(marker => marker.setMap(null)); // 將每個標記從地圖移除
    markers = []; // 清空標記陣列
}

//取得搜尋結果
function getSearchResult() {
    return $.ajax({
        url: '/booking/api/search_result',
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
    });
}
//取得新地點
function updateSearchResult(currentCenter) {
    return $.ajax({
        url: '/booking/api/map_search',
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify({
            lat: currentCenter.lat,
            lnt: currentCenter.lng
        }), success: function (data) {
            console.log('成功')
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.error('AJAX 請求發生錯誤:', textStatus, errorThrown);
            console.log('響應文本:', jqXHR.responseText);
        }
    });
}

function haversineDistance(lat1, lng1, lat2, lng2) {
    const R = 6371; // 地球半徑，單位：公里
    const toRad = angle => angle * Math.PI / 180;

    const dLat = toRad(lat2 - lat1);
    const dLng = toRad(lng2 - lng1);
    const a = Math.sin(dLat / 2) ** 2 +
        Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
        Math.sin(dLng / 2) ** 2;
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
}

function sortByDistance(data, centerLat, centerLng) {
    data.hotels.sort((a, b) => {
        const distanceA = haversineDistance(centerLat, centerLng, a.lat, a.lng);
        const distanceB = haversineDistance(centerLat, centerLng, b.lat, b.lng);
        return distanceA - distanceB;
    });
}

function sortByPrice(data, order = 'asc') {
    data.hotels.sort((a, b) => {
        if (order === 'asc') {
            return a.lowestTotalPrice - b.lowestTotalPrice;
        } else {
            return b.lowestTotalPrice - a.lowestTotalPrice;
        }
    });
}

