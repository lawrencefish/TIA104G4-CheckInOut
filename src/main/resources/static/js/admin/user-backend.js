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
	        init() {
	            this.initializeTables();
	            this.setupTableToggle();
	            this.setupSearch();
	            this.setupStatusUpdates();
				this.trackLoginTime();
	        },

	        // 初始化 DataTables
	        initializeTables() {
	            const commonConfig = {
	                pageLength: 10,
	                lengthMenu: [[10, 25, 50, 100], [10, 25, 50, 100]],
	                language: {
	                    "sProcessing": "處理中...",
	                    "sLengthMenu": "顯示 _MENU_ 筆",
	                    "sZeroRecords": "沒有匹配結果",
	                    "sInfo": "顯示第 _START_ 至 _END_ 筆結果，共 _TOTAL_ 筆",
	                    "sInfoEmpty": "顯示第 0 至 0 筆結果，共 0 筆",
	                    "sInfoFiltered": "(從 _MAX_ 項結果過濾)",
	                    "oPaginate": {
	                        "sFirst": "首頁",
	                        "sPrevious": "上頁",
	                        "sNext": "下頁",
	                        "sLast": "末頁"
	                    }
	                }
	            };
				
				

	            // 初始化業者表格
	            this.businessTable = $('#businessTable').DataTable(commonConfig);
	            
	            // 初始化會員表格
	            this.memberTable = $('#memberTable').DataTable(commonConfig);
	        },

	        // 設置表格切換
			setupTableToggle() {
			    const businessBtn = $('#businessNameBtn');
			    const memberBtn = $('#memberNameBtn');
			    const businessTable = $('#businessTable_wrapper');
			    const memberTable = $('#memberTable_wrapper');

			    businessBtn.click(function () {
			        $(this).addClass('active');
			        memberBtn.removeClass('active');
			        businessTable.show();
			        memberTable.hide();
			    });

			    memberBtn.click(function () {
			        $(this).addClass('active');
			        businessBtn.removeClass('active');
			        memberTable.show();
			        businessTable.hide();
			    });

			    // 預設顯示業者表格，隱藏會員表格
			    businessTable.show();
			    memberTable.hide();
			},

	        // 設置搜尋功能
	        setupSearch() {
	            const self = this;
	            
	            // 全局搜尋
	            $('#globalSearch').on('keyup', function() {
	                const searchValue = $(this).val();
	                const activeTable = $('#businessTable_wrapper').is(':visible') ? 
	                    self.businessTable : self.memberTable;
	                activeTable.search(searchValue).draw();
	            });

	            // 狀態篩選
	            $('#statusFilter').on('change', function() {
	                const statusValue = $(this).val();
	                const activeTable = $('#businessTable_wrapper').is(':visible') ? 
	                    self.businessTable : self.memberTable;
	                
	                // 使用自定義過濾器
	                $.fn.dataTable.ext.search.push(function(settings, data) {
	                    if (!statusValue) return true; // 如果沒有選擇狀態，顯示所有記錄
	                    
	                    const rowStatus = data[2]; // 假設狀態在第三列
	                    if (statusValue === '0' && rowStatus === '待審核') return true;
	                    if (statusValue === '1' && rowStatus === '啟用中') return true;
	                    if (statusValue === '2' && rowStatus === '停權') return true;
	                    return false;
	                });
	                
	                activeTable.draw();
	                
	                // 清除過濾器
	                $.fn.dataTable.ext.search.pop();
	            });
	        },

	        // 設置狀態更新功能
	        setupStatusUpdates() {
				$('.btn-status-update').click(function () {
				    const id = $(this).data('id');
				    const currentStatus = $(this).data('status');
				    const newStatus = currentStatus === 0 ? 1 : 0;
				    const buttonText = newStatus === 0 ? '啟用' : '停權'; // 按照需求改變按鈕文字

				    if (!confirm(`確定要將狀態更改為 ${buttonText} 嗎？`)) return;

				    const apiPath = $('#businessTable_wrapper').is(':visible')
				        ? '/adminHotel/updateStatus'
				        : '/adminMember/updateStatus';

				    fetch(apiPath, {
				        method: 'POST',
				        headers: { 'Content-Type': 'application/json' },
				        body: JSON.stringify({ id, status: newStatus })
				    })
				        .then(response => response.json())
				        .then(data => {
				            if (data.success) {
				                $(this).data('status', newStatus).text(buttonText); // 更新按鈕文字
				            } else {
				                alert('更新失敗：' + data.message);
				            }
				        })
				        .catch(error => {
				            console.error('更新狀態時發生錯誤：', error);
				            alert('更新失敗，請稍後再試');
				        });
				});
	        },
			
			// 追踪登入時間
	        trackLoginTime() {
	            // 取得所有會員的登入時間元素
	            const loginTimeElements = document.querySelectorAll('.member-login-time');
	            
	            loginTimeElements.forEach(element => {
	                // 如果元素內容為空或是初始值，設置當前時間
	                if (!element.textContent || element.textContent.trim() === '') {
	                    const currentTime = new Date();
	                    const formattedTime = currentTime.toLocaleString('zh-TW', {
	                        year: 'numeric',
	                        month: '2-digit',
	                        day: '2-digit',
	                        hour: '2-digit',
	                        minute: '2-digit',
	                        second: '2-digit'
	                    });
	                    element.textContent = formattedTime;
	                }
	            });
	        }
			   
	    };

	    // 初始化應用
	    UserManagementApp.init();
	});