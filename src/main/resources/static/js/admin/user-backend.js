document.addEventListener('DOMContentLoaded',async ()  => {

    // 等待 header.js 中的 loadCommonElements 完成
    await new Promise(resolve => {
        const checkHeader = setInterval(() => {
            if (document.querySelector('.admin-profile-trigger')) {
                clearInterval(checkHeader);
                resolve();
            }
        }, 100);
    });

    const UserManagementApp = {
        // 假資料區塊 - 之後可替換為資料庫資料
        data: {
            userDataBusiness: [
                { name: "台北君悅酒店", status: "啟用中", pending: "無", phone: "02-2720-1234", email: "info@grandhtpe.com", lastLogin: "2024-03-15" },
                { name: "台中日月千禧酒店", status: "啟用中", pending: "無", phone: "04-3705-6000", email: "service@millennium.com", lastLogin: "2024-03-14" },
                { name: "高雄漢來大飯店", status: "啟用中", pending: "無", phone: "07-216-1766", email: "contact@grand-hilai.com", lastLogin: "2024-03-13" },
                { name: "礁溪老爺酒店", status: "啟用中", pending: "無", phone: "03-988-6788", email: "service@hotelroyal.com.tw", lastLogin: "2024-03-12" },
                { name: "台南晶英酒店", status: "啟用中", pending: "無", phone: "06-213-5555", email: "info@silksplace.com.tw", lastLogin: "2024-03-11" },
                { name: "陽明山天籟溫泉會館", status: "尚未審核", pending: "待審核", phone: "02-2862-0222", email: "service@tienlai.com.tw", lastLogin: "2024-03-10" },
                { name: "花蓮遠雄悅來大飯店", status: "啟用中", pending: "無", phone: "03-812-3456", email: "info@farglory-hotel.com.tw", lastLogin: "2024-03-09" },
                { name: "墾丁福華渡假飯店", status: "停權", pending: "無", phone: "08-886-2345", email: "service@howard-kenting.com.tw", lastLogin: "2024-03-08" },
                { name: "台北W飯店", status: "啟用中", pending: "無", phone: "02-7703-8888", email: "info@wtaipei.com", lastLogin: "2024-03-07" },
                { name: "日月潭涵碧樓", status: "啟用中", pending: "無", phone: "04-9285-5311", email: "contact@thelalu.com.tw", lastLogin: "2024-03-06" },
                { name: "台中林酒店", status: "尚未審核", pending: "待審核", phone: "04-2326-8008", email: "service@thelin.com.tw", lastLogin: "2024-03-05" },
                { name: "金普頓大安酒店", status: "啟用中", pending: "無", phone: "02-2173-7999", email: "info@kimpton-taipei.com", lastLogin: "2024-03-04" },
                { name: "北投麗禧溫泉酒店", status: "啟用中", pending: "無", phone: "02-2898-5555", email: "service@gmhtresort.com", lastLogin: "2024-03-03" },
                { name: "台南大員皇冠假日酒店", status: "停權", pending: "無", phone: "06-391-1899", email: "info@crowntainan.com", lastLogin: "2024-03-02" },
                { name: "宜蘭力麗威斯汀度假酒店", status: "啟用中", pending: "無", phone: "03-960-0000", email: "contact@westin-yilan.com", lastLogin: "2024-03-01" },
                { name: "台東桂田喜來登酒店", status: "啟用中", pending: "無", phone: "089-345-6789", email: "service@sheraton-taitung.com", lastLogin: "2024-02-28" },
                { name: "澎湖福朋喜來登酒店", status: "啟用中", pending: "無", phone: "06-927-8888", email: "info@fourpoints-penghu.com", lastLogin: "2024-02-27" },
                { name: "新竹英迪格酒店", status: "尚未審核", pending: "待審核", phone: "03-658-9999", email: "contact@indigo-hsinchu.com", lastLogin: "2024-02-26" },
                { name: "嘉義兆品酒店", status: "啟用中", pending: "無", phone: "05-362-1111", email: "service@maison-chiayi.com", lastLogin: "2024-02-25" },
                { name: "基隆長榮桂冠酒店", status: "啟用中", pending: "無", phone: "02-2427-9999", email: "info@evergreen-keelung.com", lastLogin: "2024-02-24" },
                { name: "南投微風山渡假飯店", status: "停權", pending: "無", phone: "049-289-8888", email: "service@breeze-nantou.com", lastLogin: "2024-02-23" },
                { name: "金門昇恆昌金湖大飯店", status: "啟用中", pending: "無", phone: "082-373-888", email: "contact@everrich-kinmen.com", lastLogin: "2024-02-22" },
                { name: "馬祖藍海度假村", status: "尚未審核", pending: "待審核", phone: "0836-55-888", email: "info@bluesea-matsu.com", lastLogin: "2024-02-21" },
                { name: "屏東小墾丁渡假村", status: "啟用中", pending: "無", phone: "08-886-1234", email: "service@kenting-resort.com", lastLogin: "2024-02-20" },
                { name: "雲林劍湖山渡假大飯店", status: "啟用中", pending: "無", phone: "05-582-6666", email: "contact@janfusun-resort.com", lastLogin: "2024-02-19" },
            ],
            userDataMember: [
                { name: "王小明", memberId: "M001", status: "啟用中", phone: "0912-345-678", email: "ming.wang@email.com", lastLogin: "2024-03-15" },
                { name: "李美玲", memberId: "M002", status: "啟用中", phone: "0923-456-789", email: "mei.li@email.com", lastLogin: "2024-03-14" },
                { name: "張大華", memberId: "M003", status: "停權", phone: "0934-567-890", email: "hua.chang@email.com", lastLogin: "2024-03-13" },
                { name: "陳雅婷", memberId: "M004", status: "啟用中", phone: "0945-678-901", email: "ya.chen@email.com", lastLogin: "2024-03-12" },
                { name: "林志明", memberId: "M005", status: "啟用中", phone: "0956-789-012", email: "zhi.lin@email.com", lastLogin: "2024-03-11" },
                { name: "黃雅琪", memberId: "M006", status: "停權", phone: "0967-890-123", email: "ya.huang@email.com", lastLogin: "2024-03-10" },
                { name: "吳建志", memberId: "M007", status: "啟用中", phone: "0978-901-234", email: "jian.wu@email.com", lastLogin: "2024-03-09" },
                { name: "謝佳玲", memberId: "M008", status: "啟用中", phone: "0989-012-345", email: "jia.xie@email.com", lastLogin: "2024-03-08" },
                { name: "周俊宏", memberId: "M009", status: "停權", phone: "0990-123-456", email: "jun.zhou@email.com", lastLogin: "2024-03-07" },
                { name: "楊雅雯", memberId: "M010", status: "啟用中", phone: "0901-234-567", email: "ya.yang@email.com", lastLogin: "2024-03-06" },
                { name: "劉建宏", memberId: "M011", status: "啟用中", phone: "0912-345-678", email: "jian.liu@email.com", lastLogin: "2024-03-05" },
                { name: "蔡美琪", memberId: "M012", status: "停權", phone: "0923-456-789", email: "mei.tsai@email.com", lastLogin: "2024-03-04" },
                { name: "鄭志偉", memberId: "M013", status: "啟用中", phone: "0934-567-890", email: "zhi.zheng@email.com", lastLogin: "2024-03-03" },
                { name: "許雅芳", memberId: "M014", status: "啟用中", phone: "0945-678-901", email: "ya.xu@email.com", lastLogin: "2024-03-02" },
                { name: "彭俊傑", memberId: "M015", status: "啟用中", phone: "0956-789-012", email: "jun.peng@email.com", lastLogin: "2024-03-01" },
                { name: "莊雅婷", memberId: "M016", status: "啟用中", phone: "0967-890-123", email: "ya.zhuang@email.com", lastLogin: "2024-02-28" },
                { name: "林佳穎", memberId: "M017", status: "停權", phone: "0978-901-234", email: "jia.lin@email.com", lastLogin: "2024-02-27" },
                { name: "陳俊宇", memberId: "M018", status: "啟用中", phone: "0989-012-345", email: "jun.chen@email.com", lastLogin: "2024-02-26" },
                { name: "黃詩涵", memberId: "M019", status: "停權", phone: "0990-123-456", email: "shi.huang@email.com", lastLogin: "2024-02-25" },
                { name: "張家豪", memberId: "M020", status: "啟用中", phone: "0901-234-567", email: "jia.zhang@email.com", lastLogin: "2024-02-24" },
                { name: "李承翰", memberId: "M021", status: "啟用中", phone: "0912-345-678", email: "cheng.li@email.com", lastLogin: "2024-02-23" },
                { name: "吳雅琳", memberId: "M022", status: "尚停權", phone: "0923-456-789", email: "ya.wu@email.com", lastLogin: "2024-02-22" },
                { name: "王志豪", memberId: "M023", status: "啟用中", phone: "0934-567-890", email: "zhi.wang@email.com", lastLogin: "2024-02-21" },
                { name: "林怡君", memberId: "M024", status: "停權", phone: "0945-678-901", email: "yi.lin@email.com", lastLogin: "2024-02-20" },
                { name: "張維倫", memberId: "M025", status: "啟用中", phone: "0956-789-012", email: "wei.zhang@email.com", lastLogin: "2024-02-19" },
                { name: "陳韻如", memberId: "M026", status: "啟用中", phone: "0967-890-123", email: "yun.chen@email.com", lastLogin: "2024-02-18" },
                { name: "劉冠廷", memberId: "M027", status: "啟用中", phone: "0978-901-234", email: "guan.liu@email.com", lastLogin: "2024-02-17" },
                { name: "王雅芝", memberId: "M028", status: "停權", phone: "0989-012-345", email: "ya.wang@email.com", lastLogin: "2024-02-16" },
                { name: "林書豪", memberId: "M029", status: "啟用中", phone: "0990-123-456", email: "shu.lin@email.com", lastLogin: "2024-02-15" },
                { name: "陳冠宇", memberId: "M030", status: "啟用中", phone: "0901-234-567", email: "guan.chen@email.com", lastLogin: "2024-02-14" }
            ]
            },

        // 保留區塊 - 用於未來從資料庫獲取資料
        async fetchDataFromDatabase() {
            try {
                // 從資料庫獲取業者資料
                // const businessResponse = await fetch('/api/businesses');
                // this.data.userDataBusiness = await businessResponse.json();

                // 從資料庫獲取會員資料
                // const memberResponse = await fetch('/api/members');
                // this.data.userDataMember = await memberResponse.json();
            } catch (error) {
                console.error('獲取資料失敗:', error);
            }
        },

        // 配置和元素選擇
        elements: {
            businessNameBtn: document.getElementById('businessNameBtn'),
            memberNameBtn: document.getElementById('memberNameBtn'),
            businessTable: document.getElementById('businessTable'),
            memberTable: document.getElementById('memberTable'),
            businessTableHeader: document.querySelector('#businessTable thead tr'),
            memberTableHeader: document.querySelector('#memberTable thead tr'),
            userTableBody: document.getElementById('userTableBody'),
            userTableBodyMember: document.getElementById('userTableBodyMember'),
            totalCountEl: document.getElementById('totalCount'),
            memberCountEl: document.getElementById('memberCount'),
            businessCountEl: document.getElementById('businessCount')
        },

        // 分頁設置
        pagination: {
            currentType: 'business'
        },

        // 初始化
        init() {
            this.setupTableToggle();
            this.initializeChart();
            this.updateStatistics();
            this.setupDetailButtonListeners();
            this.renderTables();
        },

        // 渲染表格
        renderTables() {
            const businessBody = document.getElementById('userTableBody');
            const memberBody = document.getElementById('userTableBodyMember');

            // 渲染業者表格
            businessBody.innerHTML = this.data.userDataBusiness.map(business => `
                <tr>
                    <td>${business.name}</td>
                    <td>
                        <span class="status-badge ${this.getStatusClass(business.status)}">
                            ${business.status}
                        </span>
                    </td>
                    <td>${business.pending}</td>
                    <td>${business.phone}</td>
                    <td>${business.email}</td>
                    <td>${business.lastLogin}</td>
                    <td>
                        <button class="btn btn-info">詳細資料</button>
                    </td>
                </tr>
            `).join('');

            // 渲染會員表格
            memberBody.innerHTML = this.data.userDataMember.map(member => `
                <tr>
                    <td>${member.name}</td>
                    <td>${member.memberId}</td>
                    <td>
                        <span class="status-badge ${this.getStatusClass(member.status)}">
                            ${member.status}
                        </span>
                    </td>
                    <td>${member.phone}</td>
                    <td>${member.lastLogin}</td>
                    <td>${member.email}</td>
                    <td>
                        <button class="btn btn-info">詳細資料</button>
                    </td>
                </tr>
            `).join('');

            // DataTables 配置
        const dataTableConfig = {
            order: [[0, 'desc']],
            pageLength: 10,
            lengthMenu: [[10, 25, 50, 100], [10, 25, 50, 100]],
            stateSave: true,
            searching: true,
            ordering: true,
            info: true,
            paging: true,
            autoWidth: false,
            responsive: true,
            // 修改 DOM 結構以確保分頁顯示
            dom: 'lrtip',
            pagingType: 'full_numbers',
            language: {
                "sProcessing": "處理中...",
                "sLengthMenu": "顯示 _MENU_ 筆",
                "sZeroRecords": "沒有匹配結果",
                "sInfo": "顯示第 _START_ 至 _END_ 筆結果，共 _TOTAL_ 筆",
                "sInfoEmpty": "顯示第 0 至 0 筆結果，共 0 筆",
                "sInfoFiltered": "(從 _MAX_ 項結果過濾)",
                "sInfoPostFix": "",
                "sSearch": "搜尋:",
                "sUrl": "",
                "sEmptyTable": "表格中無可用數據",
                "sLoadingRecords": "載入中...",
                "sInfoThousands": ",",
                "oPaginate": {
                    "sFirst": "首頁",
                    "sPrevious": "上頁",
                    "sNext": "下頁",
                    "sLast": "末頁"
                }
            },
            

    
            // 確保初始化完成後分頁正確顯示
            drawCallback: function(settings) {
                const api = this.api();
                if (api.page.info().pages > 1) {
                    $('.dataTables_paginate').show();
                }
            }
        };

        // // 設置搜尋和篩選功能
        // setupSearchAndFilter() {
        //     const searchInput = document.getElementById('globalSearch');
        //     const searchBtn = document.getElementById('searchBtn');
        //     const statusFilter = document.getElementById('statusFilter');

        //     // 搜尋按鈕點擊事件
        //     searchBtn.addEventListener('click', () => {
        //         this.performSearch();
        //     });

        //     // Enter 鍵搜尋
        //     searchInput.addEventListener('keypress', (e) => {
        //         if (e.key === 'Enter') {
        //             this.performSearch();
        //         }
        //     });

        //     // 狀態篩選變更事件
        //     statusFilter.addEventListener('change', () => {
        //         this.performSearch();
        //     });
        // }
    


        

        

            // 初始化表格
            if ($.fn.DataTable.isDataTable('#businessTable')) {
                $('#businessTable').DataTable().destroy();
            }
            if ($.fn.DataTable.isDataTable('#memberTable')) {
                $('#memberTable').DataTable().destroy();
            }

            const businessTable = $('#businessTable').DataTable(dataTableConfig);
            const memberTable = $('#memberTable').DataTable(dataTableConfig);

            // 創建共用的分頁控制容器
            const paginationContainer = $('<div class="table-pagination"></div>');
            $('.table-wrapper').append(paginationContainer);

            // 移動控制元素到共用容器
            const firstControls = $('.bottom-controls').first().detach();
            paginationContainer.append(firstControls);
            $('.bottom-controls').remove();

            // 新增跳頁功能
            const pageJumpContainer = $(`
                <div class="page-jump">
                    <span>跳至第</span>
                    <input type="number" 
                           min="1" 
                           class="page-jump-input"
                           id="pageJumpInput"
                           name="pageJumpInput"
                           autocomplete="off">
                    <span>頁</span>
                    <button class="page-jump-btn">確定</button>
                </div>
            `);
            firstControls.append(pageJumpContainer);

            // 同步分頁控制
            function syncPagination(sourceTable, targetTable) {
                const currentPage = sourceTable.page();
                const pageLength = sourceTable.page.len();
                targetTable.page.len(pageLength);
                targetTable.page(currentPage);
            }

            // 搜尋功能同步
            $('.dataTables_filter input').on('keyup', function() {
                const searchValue = $(this).val();
                businessTable.search(searchValue).draw();
                memberTable.search(searchValue).draw();
            });

            // 排序功能
            $('th').on('click', function() {
                const columnIdx = $(this).index();
                const activeTable = $('#businessTable').is(':visible') ? businessTable : memberTable;
                const inactiveTable = $('#businessTable').is(':visible') ? memberTable : businessTable;
                
                const order = activeTable.order();
                inactiveTable.order(order).draw();
            });

            // 分頁控制事件
            $('.dataTables_paginate').on('click', 'a.paginate_button', function(e) {
                e.preventDefault();
                const activeTable = $('#businessTable').is(':visible') ? businessTable : memberTable;
                const inactiveTable = $('#businessTable').is(':visible') ? memberTable : businessTable;
                
                setTimeout(() => {
                    syncPagination(activeTable, inactiveTable);
                }, 0);
            });

            // 表格切換
            $('#businessNameBtn, #memberNameBtn').on('click', function() {
                const isBusinessTable = $(this).attr('id') === 'businessNameBtn';
                const currentTable = isBusinessTable ? businessTable : memberTable;
                const otherTable = isBusinessTable ? memberTable : businessTable;

                // 同步所有狀態
                const searchValue = $('.dataTables_filter input').val();
                const order = currentTable.order();
                const pageInfo = currentTable.page.info();

                otherTable.search(searchValue);
                otherTable.order(order);
                otherTable.page(pageInfo.page);
                
                syncPagination(currentTable, otherTable);
            });

            // 跳頁功能
            $('.page-jump-btn').on('click', function() {
                const input = $('.page-jump-input');
                const pageNum = parseInt(input.val()) - 1;
                const activeTable = $('#businessTable').is(':visible') ? businessTable : memberTable;
                const inactiveTable = $('#businessTable').is(':visible') ? memberTable : businessTable;
                
                const pageInfo = activeTable.page.info();
                const maxPage = pageInfo.pages;

                if (pageNum >= 0 && pageNum < maxPage) {
                    activeTable.page(pageNum).draw('page');
                    inactiveTable.page(pageNum);
                    input.val('');
                } else {
                    alert(`請輸入有效的頁碼（1-${maxPage}）`);
                }
            });

            // 每頁顯示數量改變
            $('.dataTables_length select').on('change', function() {
                const newLength = $(this).val();
                businessTable.page.len(newLength).draw();
                memberTable.page.len(newLength).draw();
            });
        },

        // 設置詳細資訊按鈕監聽器
        setupDetailButtonListeners() {
            const tableBody = document.getElementById('userTableBody');
            const tableBodyMember = document.getElementById('userTableBodyMember');

            // 業者詳細資訊事件委派
            tableBody.addEventListener('click', (e) => {
                if (e.target.classList.contains('btn-info') && this.pagination.currentType === 'business') {
                    const row = e.target.closest('tr');
                    const name = row.querySelector('td:first-child').textContent;
                    const businessData = this.data.userDataBusiness.find(business => business.name === name);
                    
                    if (businessData) {
                        window.location.href = `industry-backend.html?name=${encodeURIComponent(name)}&email=${encodeURIComponent(businessData.email)}&phone=${encodeURIComponent(businessData.phone)}`;
                    }
                }
            });

            // 會員詳細資訊事件委派
            tableBodyMember.addEventListener('click', (e) => {
                if (e.target.classList.contains('btn-info') && this.pagination.currentType === 'member') {
                    const row = e.target.closest('tr');
                    const name = row.querySelector('td:first-child').textContent;
                    const memberId = row.querySelector('td:nth-child(2)').textContent;
                    const memberData = this.data.userDataMember.find(member => member.name === name);
                    
                    if (memberData) {
                        window.location.href = `member-backend.html?name=${encodeURIComponent(name)}&memberId=${encodeURIComponent(memberId)}&email=${encodeURIComponent(memberData.email)}&phone=${encodeURIComponent(memberData.phone)}`;
                    }
                }
            });
        },

        // 取得狀態對應的 CSS class
        getStatusClass(status) {
            switch(status) {
                case '啟用中':
                    return 'status-active';
                case '尚未審核':
                    return 'status-pending';
                case '停權':
                    return 'status-disabled';
                default:
                    return '';
            }
        },


        // 表格切換
        setupTableToggle() {
            const { businessNameBtn, memberNameBtn, businessTable, memberTable } = this.elements;

            businessTable.style.display = 'table';
            memberTable.style.display = 'none';
            businessNameBtn.classList.add('active');

            businessNameBtn.addEventListener('click', () => {
                businessNameBtn.classList.add('active');
                memberNameBtn.classList.remove('active');
                businessTable.style.display = 'table';
                memberTable.style.display = 'none';
                this.pagination.currentType = 'business';
            });

            memberNameBtn.addEventListener('click', () => {
                memberNameBtn.classList.add('active');
                businessNameBtn.classList.remove('active');
                memberTable.style.display = 'table';
                businessTable.style.display = 'none';
                this.pagination.currentType = 'member';
            });
        },

        // 更新統計數據
        updateStatistics() {
            const totalMembers = 1960;
            const totalBusinesses = 250;

            this.elements.totalCountEl.textContent = totalMembers;
            this.elements.memberCountEl.textContent = totalMembers - totalBusinesses;
            this.elements.businessCountEl.textContent = totalBusinesses;
        },

        // 初始化圖表
        initializeChart() {
            const ctx = document.getElementById('combinedChart').getContext('2d');
            
            const months = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'];
            const newMembers = [50, 60, 70, 80, 120, 150, 200, 180, 170, 160, 140, 130];
            const totalMembers = [500, 560, 630, 710, 830, 980, 1180, 1360, 1530, 1690, 1830, 1960];

            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: months,
                    datasets: [
                        {
                            type: 'bar',
                            label: '每月新增會員數',
                            data: newMembers,
                            backgroundColor: 'rgba(54, 162, 235, 0.6)',
                        },
                        {
                            type: 'line',
                            label: '總會員數',
                            data: totalMembers,
                            borderColor: 'rgba(255, 99, 132, 1)',
                            backgroundColor: 'rgba(255, 99, 132, 0.2)',
                        },
                    ],
                },
                options: {
                    responsive: true,
                    plugins: {
                        title: {
                            display: true,
                            text: '會員人數分析圖表',
                        },
                    },
                },
            });
        }
    };

    // 初始化應用
    UserManagementApp.init();

    // 表格切換功能
    $('#businessNameBtn').on('click', function() {
        $(this).addClass('active');
        $('#memberNameBtn').removeClass('active');
        $('#businessTable_wrapper').show();
        $('#memberTable_wrapper').hide();
    });

    $('#memberNameBtn').on('click', function() {
        $(this).addClass('active');
        $('#businessNameBtn').removeClass('active');
        $('#memberTable_wrapper').show();
        $('#businessTable_wrapper').hide();
    });

    // 初始顯示業者表格
    $('#businessNameBtn').click();
});