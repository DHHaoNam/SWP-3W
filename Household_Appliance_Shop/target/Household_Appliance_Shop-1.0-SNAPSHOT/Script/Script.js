var header = document.querySelector(".menu-nav"); // Lấy phần tử đầu tiên với class .menu-nav
var btns = header.getElementsByClassName("menu-btn"); // Lấy tất cả các button với class .menu-btn

for (var i = 0; i < btns.length; i++) {
    btns[i].addEventListener("click", function () {
        // Tìm phần tử hiện đang có class 'active'
        var current = document.querySelector(".menu-btn.active");
        
        // Loại bỏ class 'active' khỏi phần tử đó
        if (current) {
            current.classList.remove("active");
        }
        
        // Thêm class 'active' cho phần tử được click
        this.classList.add("active");
    });
}

document.addEventListener("DOMContentLoaded", function () {
    const userIcon = document.querySelector(".fa-user");
    const userMenu = document.getElementById("userMenu");

    userIcon.addEventListener("click", function () {
        if (userMenu.style.display === "none" || userMenu.style.display === "") {
            userMenu.style.display = "block";
        } else {
            userMenu.style.display = "none";
        }
    });

    // Đóng menu khi click ra ngoài
    document.addEventListener("click", function (event) {
        if (!userIcon.contains(event.target) && !userMenu.contains(event.target)) {
            userMenu.style.display = "none";
        }
    });
});

