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
    ({ key: "AIzaSyAQ4SS_rzxn4J8dPktZjUiVMAkjGA_dCuo", v: "weekly" });

// 定義地圖和其他全域變數
let map; // 儲存地圖物件
let currentCenter; // 儲存目前地圖的中心位置
let markers = []; // 儲存地圖上的標記

// 初始化地圖的主函式
async function initMap() {
    const { Map } = await google.maps.importLibrary("maps"); // 載入地圖庫
    const myLatlng = { lat: 25.03298, lng: 121.5654 }; // 設定初始的地圖中心點（台北市）
    const { AdvancedMarkerElement } = await google.maps.importLibrary("marker"); // 載入標記庫

    // 初始化地圖並設定相關屬性
    map = new Map(document.getElementById("map"), {
        zoom: 15, // 設定地圖縮放層級
        center: myLatlng, // 設定地圖的中心點
        mapId: "dd77e8c7f72def60", // 自訂的地圖 ID
        disableDefaultUI: true,
    });

    currentCenter = myLatlng; // 設定目前的地圖中心點
    hotelLoading(currentCenter, map); // 初始載入飯店資訊

    // 當地圖閒置（idle）時，重新載入飯店資訊
    map.addListener("idle", () => {
        const newCenter = map.getCenter(); // 取得當前地圖的中心位置
        const centerLatLng = {
            lat: newCenter.lat(),
            lng: newCenter.lng(),
        };

        // 如果地圖中心點改變，就重新載入資料
        if (
            !currentCenter || // 如果是第一次載入
            currentCenter.lat !== centerLatLng.lat || // 檢查緯度是否改變
            currentCenter.lng !== centerLatLng.lng // 檢查經度是否改變
        ) {
            currentCenter = centerLatLng; // 更新目前的中心點
            clearMarkers(); // 清除舊的標記
            hotelLoading(centerLatLng, map); // 載入新的飯店資訊
        }
    });
}

function addCardClickEvent(hotelID, position) {
    const targetCard = document.getElementById(`card-${hotelID}`);
    if (targetCard) {
        targetCard.addEventListener("click", () => {
            map.setZoom(16); // 設定地圖縮放層級
            map.panTo(position); // 將地圖移動到指定位置
        });
    }
}


// 載入飯店資訊並顯示在地圖上
// 修改後的 hotelLoading 函式，加入氣泡點擊事件
function hotelLoading(center, map) {
    let ListURL = `https://maps.googleapis.com/maps/api/place/nearbysearch/json`;
    let keyword = `?keyword=飯店`; // 搜尋關鍵字
    let location = `${center.lat}%2C${center.lng}`; // 中心點座標
    let radius = `1000`; // 搜尋範圍（半徑 1000 公尺）
    let type = `hotel`; // 搜尋類型：飯店
    let key = `AIzaSyAQ4SS_rzxn4J8dPktZjUiVMAkjGA_dCuo`;

    // 呼叫 Google Maps Places API 並處理回應資料
    fetch(ListURL + `${keyword}&location=${location}&radius=${radius}&type=${type}&key=${key}`)
        .then((res) => {
            if (res.ok) return res.json(); // 如果回應成功，解析成 JSON
        })
        .then((data) => {
            console.log(`Hotels reloaded:`);
            $(".card-scroll").empty(); // 清空舊的飯店清單

            // 處理每個飯店的資料
            data.results.forEach((ele) => {
                let hotelName = ele.name;
                let hotelReview = ele.rating;
                let hotelReviewConut = ele.user_ratings_total;
                let hotelCity = ele.vicinity;
                let hotelPrice = 100; // 預設價格
                let hotelID = ele.place_id; // 飯店 ID
                let hotelIMG = `https://maps.googleapis.com/maps/api/place/photo?maxheight=100&photoreference=${ele.photos[0].photo_reference}&key=${key}`;
                let position = ele.geometry.location; // 標記的位置

                // 建立飯店卡片
                let hotelCard = `
                        <div class="col ${hotelID}">
                            <div class="card h-100 my-3" id="card-${hotelID}">
                                <div class="row g-0">
                                    <div class="col-4" style="height:200px; overflow: hidden;">
                                        <img src="${hotelIMG}" style="width:100%; height:100%; object-fit:cover;" alt="${hotelName}">
                                    </div>
                                    <div class="col-8">
                                        <div class="card-body">
                                            <h5 class="card-title mb-1">${hotelName}</h5>
                                            <div class="card-text mb-4">
                                                <span class="badge bg-primary"><span>${hotelReview}</span>分 </span>
                                                <span class="badge bg-secondary"><span>${hotelReviewConut}</span>則評論</span>
                                                <span class="badge bg-success">${hotelCity}</span>
                                            </div>
                                            <div class="card-footer bg-body border-0 p-0">
                                                <p class="hotel-price h4">NT$<span class="price">${hotelPrice}</span></p>
                                                <button class="btn btn-primary">立刻訂房</button>
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
                    position: ele.geometry.location,
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
        });
}

// 清除地圖上的標記
function clearMarkers() {
    markers.forEach(marker => marker.setMap(null)); // 將每個標記從地圖移除
    markers = []; // 清空標記陣列
}

// 初始化地圖
initMap();

// // 監聽點擊事件，用來偵測使用者點擊的元素
// document.querySelector('html').addEventListener('click', function (e) {
//     e.stopPropagation(); // 停止事件冒泡

//     // 檢查是否點擊到了目標範圍
//     if (e.target) {
//         console.log('點擊的元素是：', e.target);
//         console.log('元素的類名：', e.target.className);
//         console.log('元素的標籤名稱：', e.target.tagName);
//     } else {
//         console.warn('點擊的元素不存在');
//     }
// });
