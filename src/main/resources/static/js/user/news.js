function newslLoading() {
    let times = 0;
    let newsTitle = 'test';
    let newsText = '9';
    let newsHref = '/news_detail';
    let newsImgSrc = '/imgs/user/uno-hotel.jpg';
    let newsPostTime = '2024-12-01';
    let newsCard = `
                <div class="card col-8 mb-3">
                    <div class="row d-flex  justify-content-center">
                        <div class="col-3">
                            <img src=${newsImgSrc} class="img-fluid rounded-start h-100">
                        </div>
                        <div class="col-9">
                            <div class="card-body m-0">
                                <h5 class="card-title">
                                     <a href=${newsHref} class="news-link" style="text-decoration-line: none; color: black;">
                                     ${newsTitle}</a>
                                </h5>
                                <p class="card-text">${newsText}</p>
                                <p class="card-text post-time"><small class="text-muted">${newsPostTime}</small></p>
                            </div>
                        </div>
                    </div>
                </div>
`;
    //捲動增加
    $('.news-list').on("scroll", function () {
        let times = 0;
        if (($('.news-list').scrollTop() + $('.news-list').height() > $('.news-list').height() - 50) && times < 15) {
            $('.news-list').append(newsCard);
            times++;
        }
    });

}

newslLoading();