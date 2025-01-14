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
  ({ key: apikey, v: "weekly" ,libraries: "places"});

let map;

//設定地圖
async function initMap() {
  let center = { lat: 23.858987, lng: 120.917631 };
  const { Map } = await google.maps.importLibrary("maps");
  const { AdvancedMarkerElement } = await google.maps.importLibrary("marker");
  map = new Map(document.getElementById("map"), {
    center: { lat: 23.858987, lng: 120.917631 },
    zoom: 8,
    mapId: 'dd77e8c7f72def60',
    gmpClickable: true,
    disableDefaultUI: true,
  });
  //設定縣市
  const input = document.getElementById("place");
  const options = {
    componentRestrictions: { country: "tw" }  // 限制在台灣
  };
  const autocomplete = new google.maps.places.Autocomplete(input, options);
  

  $.getJSON("/vendors/twCity.json", function (e) {
    let features = e.features;
    let taiwan = [];      // 行政區域多邊形特徵值的陣列
    let name = [];        // 行政區域名稱的陣列
    let polygonPath = []; // 繪製後的多邊形陣列
    features.forEach(function (i, index) {
      let arr = [];
      name.push(i.properties.name); // 將各個行政區的名字記錄到 name 陣列中
      if (i.geometry.coordinates.length == 1) {
        // 如果行政區域只有一塊，例如南投縣
        i.geometry.coordinates[0].forEach(function (j) {
          arr.push({
            lat: j[1],
            lng: j[0]
          });
        });
        taiwan.push(arr);
      } else {
        // 如果行政區域不只一塊，例如台東縣包含綠島和蘭嶼，就是個多邊形集合
        for (let k = 0; k < i.geometry.coordinates.length; k++) {
          var arrContent = [];
          if (i.geometry.coordinates[k].length == 1) {
            //如果行政區域沒有包含其他的行政區域，例如台東縣
            i.geometry.coordinates[k][0].forEach(function (j) {
              arrContent.push({
                lat: j[1],
                lng: j[0]
              });
            });
          } else {
            //如果行政區域包含了其他的行政區域，例如嘉義縣包覆著嘉義市
            i.geometry.coordinates[k].forEach(function (j) {
              arrContent.push({
                lat: j[1],
                lng: j[0]
              });
            });
          }
          arr.push(arrContent);
        }
        taiwan.push(arr);
      }

      // 依序在地圖上畫出對應的多邊形
      polygonPath[index] = new google.maps.Polygon({
        paths: arr,
        strokeColor: '#000',
        strokeOpacity: .7,
        strokeWeight: 1,
        strokePosition: google.maps.StrokePosition.CENTER,
        fillColor: '#fff',
        fillOpacity: 0.35,
        map: map
      });

      // 為每個多邊形加上滑鼠點擊事件
      polygonPath[index].addListener('click', function (e) {
        // 點擊時獲取滑鼠的經緯度座標
        let coordinate = { lat: e.latLng.lat(), lng: e.latLng.lng() };
        $("#place").val(name[index]);
        console.log(name[index]);
        // 將資訊視窗打開在地圖上
      });

    })
  });

  //地標設定
  const mapCityData = {
    "locations": [
      { "name": "新北市", "longitude": 121.5367, "latitude": 24.8280 },
      { "name": "高雄市", "longitude": 120.666, "latitude": 22.9377 },
      { "name": "臺中市", "longitude": 120.69080, "latitude": 24.15115 },
      { "name": "臺北市", "longitude": 121.5685, "latitude": 25.06296 },
      { "name": "桃園縣", "longitude": 121.2168, "latitude": 24.93759 },
      { "name": "臺南市", "longitude": 120.30628, "latitude": 23.10932 },
      { "name": "彰化縣", "longitude": 120.46558, "latitude": 23.90541 },
      { "name": "屏東縣", "longitude": 120.60, "latitude": 22.54951 },
      { "name": "雲林縣", "longitude": 120.3897, "latitude": 23.6592 },
      { "name": "苗栗縣", "longitude": 120.9417, "latitude": 24.48927 },
      { "name": "嘉義縣", "longitude": 120.65298, "latitude": 23.38190 },
      { "name": "新竹縣", "longitude": 121.19503, "latitude": 24.60932 },
      { "name": "南投縣", "longitude": 120.9876, "latitude": 23.83876 },
      { "name": "宜蘭縣", "longitude": 121.6576, "latitude": 24.5385 },
      { "name": "新竹市", "longitude": 120.9423, "latitude": 24.78399 },
      { "name": "基隆市", "longitude": 121.7081, "latitude": 25.10898 },
      { "name": "花蓮縣", "longitude": 121.44539, "latitude": 23.82825 },
      { "name": "嘉義市", "longitude": 120.4473, "latitude": 23.47545 },
      { "name": "臺東縣", "longitude": 121.1027, "latitude": 22.9831 },
      { "name": "金門縣", "longitude": 118.3186, "latitude": 24.43679 },
      { "name": "澎湖縣", "longitude": 119.6151, "latitude": 23.56548 },
      { "name": "連江縣", "longitude": 119.9502, "latitude": 26.16157 }
    ]
  };
  // 迭代 locations 陣列，為每個縣市創建一個 <div>
  mapCityData.locations.forEach(location => {
    const markerContent = document.createElement("div");
    markerContent.textContent = location.name; // 標記內容為縣市名稱
    markerContent.classList.add("landmark"); // 標記內容為縣市名稱
    markerContent.style.fontSize = "12px";
    markerContent.style.color = "black";
    markerContent.style.padding = "1px";

    // 創建 AdvancedMarkerElement
    const marker = new google.maps.marker.AdvancedMarkerElement({
      map,
      position: { lat: location.latitude, lng: location.longitude },
      content: markerContent,
      gmpClickable: true,
    });
  });
};

initMap();


function clickOnNum(e) {
  let num = $(this).siblings("input[type='text']");
  let currentVal = parseInt(num.val());
  if (!currentVal) currentVal = 0;
  if ($(this).hasClass('plus')) {
    if (currentVal >= 10) {
      showModal("數量不能大於10");
      currentVal = 10;
    } else {
      num.val(currentVal + 1);
    }
  } else if ($(this).hasClass('minus') && currentVal > 1) {
    num.val(currentVal - 1);
  }
  if ($('#roomNum').val() > $('#guestNum').val() && $('#guestNum').val() != "" && $('#roomNum').val() != "") {
    showModal("房數大於入住人數，請重新輸入");
    $('#roomNum').val($('#guestNum').val());
  }
}

//日曆處理
// 當文件載入完成後執行初始化
$(document).ready(function () {
  // 點擊日期範圍顯示區域時切換日曆的顯示狀態
  $('#date-range').on('click', function (e) {
    e.stopPropagation();
    const $calendar = $('#calendar-wrapper');
    $calendar.toggleClass('d-none');
  });

  // 點擊日曆和日期範圍顯示區域以外的地方時關閉日曆
  $(document).on('click', function (e) {
    if (!$(e.target).closest('#calendar-wrapper, #date-range').length) {
      $('#calendar-wrapper').addClass('d-none');
    }
  });

  $('.room_num').find('.plus').on('click', clickOnNum);
  $('.room_num').find('.minus').on('click', clickOnNum);
  $('.people_num').find('.plus').on('click', clickOnNum);
  $('.people_num').find('.minus').on('click', clickOnNum);

  $('#roomNum').on('input', function (e) {
    e.stopPropagation();
    if (e.target.value > 10) {
      showModal("數量不能大於10");
      $('#roomNum').val(10);
    }
    if ($('#roomNum').val() > $('#guestNum').val() && $('#guestNum').val() != "" && $('#roomNum').val() != "") {
      showModal("房數大於入住人數，請重新輸入");
      $('#roomNum').val($('#guestNum').val());
    }
  })


  $('#guestNum').on('input', function (e) {
    e.stopPropagation();
    if (e.target.value > 10) {
      showModal("數量不能大於10");
      $('#guestNum').val(10);
    }
    if ($('#roomNum').val() > $('#guestNum').val() && $('#guestNum').val() != "" && $('#roomNum').val() != "") {
      showModal("房數大於入住人數，請重新輸入");
      $('#guestNum').val($('#roomNum').val());
    }
  });

  $('#sumbitBtn').on('click', (e) => {
    let start_date = $('#start_date').text();
    let end_date = $('#end_date').text();
    if (start_date != "" && end_date != "") {
      e.preventDefault();
      fetchBooking();
    } else {
      showModal("請選取入住日期跟退房日期");
      $('#date-range').text("選擇入住跟退房日期");

    }
  })


});

function fetchBooking() {
  $.ajax({
    url: '/booking/api/search',
    type: 'POST',
    data: JSON.stringify({
      guestNum: $('#guestNum').val(),
      roomNum: $('#roomNum').val(),
      checkInDate: $('#start_date').text(),
      checkOutDate: $('#end_date').text(),
      place: $('#place').val(),
    }),
    contentType: 'application/json',
    dataType: 'json',
    success: function (data) {
      window.location.href = data.url;
    },
    error: function (xhr, textStatus, errorThrown) {
      console.error('Error:', textStatus, errorThrown);
    },
  });
}
