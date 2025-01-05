//地圖初始設定
(g => { var h, a, k, p = "The Google Maps JavaScript API", c = "google", l = "importLibrary", q = "__ib__", m = document, b = window; b = b[c] || (b[c] = {}); var d = b.maps || (b.maps = {}), r = new Set, e = new URLSearchParams, u = () => h || (h = new Promise(async (f, n) => { await (a = m.createElement("script")); e.set("libraries", [...r] + ""); for (k in g) e.set(k.replace(/[A-Z]/g, t => "_" + t[0].toLowerCase()), g[k]); e.set("callback", c + ".maps." + q); a.src = `https://maps.${c}apis.com/maps/api/js?` + e; d[q] = f; a.onerror = () => h = n(Error(p + " could not load.")); a.nonce = m.querySelector("script[nonce]")?.nonce || ""; m.head.append(a) })); d[l] ? console.warn(p + " only loads once. Ignoring:", g) : d[l] = (f, ...n) => r.add(f) && u().then(() => d[l](f, ...n)) })({
  key: "AIzaSyAQ4SS_rzxn4J8dPktZjUiVMAkjGA_dCuo",
  v: "weekly",
});
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
        console.log(coordinate);
        $("#place").val(name[index]);
        console.log(coordinate);
        console.log(name[index]);
        // 將資訊視窗打開在地圖上
      });

    })
  });

  //地標設定
  const data = {
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
  data.locations.forEach(location => {
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
  console.log(currentVal)
  if ($(this).hasClass('plus')) {
    num.val(currentVal + 1);
  } else if ($(this).hasClass('minus') && currentVal > 1) {
    num.val(currentVal - 1);
  }
}

$('.room_num').find('.plus').on('click', clickOnNum);
$('.room_num').find('.minus').on('click', clickOnNum);
$('.people_num').find('.plus').on('click', clickOnNum);
$('.people_num').find('.minus').on('click', clickOnNum);