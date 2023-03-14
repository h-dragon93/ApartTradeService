function loginWithKakao() {
    $.ajax({
        url: '/login/getKakaoAuthCode',
        type: 'get',
    }).done(function (res) {
        location.href = res;
    });
}