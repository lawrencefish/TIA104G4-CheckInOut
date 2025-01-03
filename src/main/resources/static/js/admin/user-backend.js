document.addEventListener('DOMContentLoaded', async () => {
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
        // DataTables 配置
        dataTableConfig: {
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
            drawCallback: function(settings) {
                const api = this.api();
                if (api.page.info().pages > 1) {
                    $('.dataTables_paginate').show();
                }
            }
        },

        // 初始化
        init() {
            this.setupTableToggle();
            this.setupSearchAndFilter();
            this.setupDetailButtonListeners();
            this.loadBusinessData(); // 加載業者資料
            this.initializeTables(); // 初始化表格
        },

        // 加載業者資料
        loadBusinessData() {
            // 發送 AJAX 請求獲取業者資料
            fetch('/hotel/getHotelsData')  // API是 /hotel/getHotelsData
                .then(data => {
                	console.log(data);
                })
                .catch(error => {
                    console.error('載入業者資料時發生錯誤:', error);
                });
        },

        // 渲染業者資料到表格
        renderBusinessTable(data) {
            // 如果 DataTable 已經初始化，先銷毀它
            if ($.fn.DataTable.isDataTable('#businessTable')) {
                $('#businessTable').DataTable().destroy();
            }

            // 初始化 DataTable 並填充業者資料
            const businessTable = $('#hotel').DataTable({
                ...this.dataTableConfig,
                data: data,  // 將獲取到的資料填充到 DataTable 中
                columns: [
                    { title: "業者ID", data: "hotelId" },
                    { title: "業者名稱", data: "hotelName" },
                    { title: "業者電話", data: "hotelPhone" },
                    // 其他欄位...
                ]
            });

            this.setupPagination(businessTable);
        },

        // 初始化表格
        initializeTables() {
            // 初始化 DataTables
            if ($.fn.DataTable.isDataTable('#businessTable')) {
                $('#businessTable').DataTable().destroy();
            }
            if ($.fn.DataTable.isDataTable('#memberTable')) {
                $('#memberTable').DataTable().destroy();
            }

            const businessTable = $('#businessTable').DataTable(this.dataTableConfig);
            const memberTable = $('#memberTable').DataTable(this.dataTableConfig);

            this.setupPagination(businessTable, memberTable);
        },

        // 設置分頁
        setupPagination(businessTable, memberTable) {
            const paginationContainer = $('<div class="table-pagination"></div>');
            $('.table-wrapper').append(paginationContainer)           

            this.setupPaginationEvents(businessTable, memberTable);
        },

        // 設置分頁事件
        setupPaginationEvents(businessTable, memberTable) {

            // 分頁大小變更
            $('.dataTables_length select').on('change', function() {
                const newLength = $(this).val();
                businessTable.page.len(newLength).draw();
                memberTable.page.len(newLength).draw();
            });
        },

        // 設置搜尋和篩選
        setupSearchAndFilter() {
            const globalSearch = document.getElementById('globalSearch');
            const searchBtn = document.getElementById('searchBtn');
            const statusFilter = document.getElementById('statusFilter');

            const performSearch = () => {
                const searchValue = globalSearch.value;
                const statusValue = statusFilter.value;

                const activeTable = $('#businessTable').is(':visible') 
                    ? $('#businessTable').DataTable()
                    : $('#memberTable').DataTable();

                // 組合搜尋條件
                let combinedSearch = '';
                if (searchValue) combinedSearch += searchValue;
                if (statusValue) {
                    combinedSearch += combinedSearch ? ' ' + statusValue : statusValue;
                }

                activeTable.search(combinedSearch).draw();
            };

            searchBtn.addEventListener('click', performSearch);
            globalSearch.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') performSearch();
            });
            statusFilter.addEventListener('change', performSearch);
        },

        // 設置詳細資訊按鈕事件
        setupDetailButtonListeners() {
            $(document).on('click', '.btn-info', function() {
                const row = $(this).closest('tr');
                const hotelId = row.data('hotelId');
                window.location.href = `/admin/industryBackend/${hotelId}`;
            });
        },

        // 表格切換
        setupTableToggle() {
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
        },
    };

    // 初始化應用
    UserManagementApp.init();
});
