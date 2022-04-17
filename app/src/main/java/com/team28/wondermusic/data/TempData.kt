package com.team28.wondermusic.data

import com.team28.wondermusic.data.models.Account
import com.team28.wondermusic.data.models.Album
import com.team28.wondermusic.data.models.Playlist
import com.team28.wondermusic.data.models.Song

object TempData {


    val accounts = arrayListOf(
        Account(
            idAccount = 0,
            email = "vinhvipit@gmail.com",
            accountName = "Quang Vinh",
            avatar = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_webp/avatars/f/2/8/d/f28d80ff4eca2403eff48dcbbf7fcb11.jpg",
            dateCreated = "01/01/2020",
            role = 0,
            accountStatus = 0,
            totalSongs = 5,
            totalLikes = 45,
            totalFollowers = 15,
            totalFollowings = 23,
        ),
        Account(
            idAccount = 1,
            email = "hongthanh@gmail.com",
            accountName = "Hồng Thanh",
            avatar = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_webp/avatars/5/6/0/6/5606a2b9c7cf2515a864785e2e1840fa.jpg",
            dateCreated = "01/01/2020",
            role = 0,
            accountStatus = 0,
            totalSongs = 3,
            totalLikes = 25,
            totalFollowers = 7,
            totalFollowings = 14,
        ),
        Account(
            idAccount = 2,
            email = "mie@gmail.com",
            accountName = "DJ Mie",
            avatar = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_webp/avatars/e/c/1/f/ec1fcdefabbc8fea32a2eb23301837d3.jpg",
            dateCreated = "01/01/2020",
            role = 0,
            accountStatus = 0,
            totalSongs = 3,
            totalLikes = 25,
            totalFollowers = 7,
            totalFollowings = 14,
        ),
        Account(
            idAccount = 3,
            email = "erik@gmail.com",
            accountName = "Erik",
            avatar = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_webp/avatars/1/c/3/1/1c316ca8b1bc893664e10bc908258420.jpg",
            dateCreated = "01/01/2020",
            role = 0,
            accountStatus = 0,
            totalSongs = 3,
            totalLikes = 25,
            totalFollowers = 7,
            totalFollowings = 14,
        ),
        Account(
            idAccount = 4,
            email = "zombie@gmail.com",
            accountName = "Zombie",
            avatar = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_webp/avatars/d/8/d/6/d8d61f4e27a6def35d31e91a3ec35f34.jpg",
            dateCreated = "01/01/2020",
            role = 0,
            accountStatus = 0,
            totalSongs = 3,
            totalLikes = 25,
            totalFollowers = 7,
            totalFollowings = 14,
        ),
        Account(
            idAccount = 5,
            email = "huongly@gmail.com",
            accountName = "Hương Ly",
            avatar = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_webp/avatars/e/f/8/4/ef84f25bebe0bb917735de836a3e417f.jpg",
            dateCreated = "01/01/2020",
            role = 0,
            accountStatus = 0,
            totalSongs = 3,
            totalLikes = 25,
            totalFollowers = 7,
            totalFollowings = 14,
        ),
        Account(
            idAccount = 6,
            email = "phong@gmail.com",
            accountName = "Châu Khải Phong",
            avatar = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_webp/avatars/9/a/e/5/9ae5c75c89064613a294f0de98bcad64.jpg",
            dateCreated = "01/01/2020",
            role = 0,
            accountStatus = 0,
            totalSongs = 3,
            totalLikes = 25,
            totalFollowers = 7,
            totalFollowings = 14,
        ),
    )

    val myAccount = accounts[0]

    val playlists = arrayListOf(
        Playlist(1, "Nhạc Hoa hay nhất", accounts[0], 0, null),
        Playlist(2, "Liên khúc khải hoàn", accounts[1], 0, null),
        Playlist(3, "Liên khúc nhạc xuân", accounts[0], 0, null),
        Playlist(4, "Chuyện tình nàng ca sĩ", accounts[3], 0, null),
        Playlist(5, "Lofi tâm trạng", accounts[4], 0, null),
    )

    val albums by lazy {
        arrayListOf(
            Album(
                1,
                "Một lần dang dở (single)",
                "01/01/2020",
                accounts[0],
                arrayListOf(songs[0], songs[1], songs[2])
            ),
            Album(
                2, "Tuổi trẻ", "01/01/2020", accounts[1],
                arrayListOf(songs[0], songs[1], songs[2])
            ),
            Album(
                3, "Xuân đến rồi (single)", "01/01/2020", accounts[0],
                arrayListOf(songs[3], songs[1], songs[2])
            ),
            Album(
                4, "Tình mẹ bao la", "01/01/2020", accounts[0],
                arrayListOf(songs[0], songs[1], songs[2])
            ),
        )
    }

    val songs = arrayListOf(
        Song(
            idSong = 1,
            name = "Chạy về khóc với anh",
            link = "https://docs.google.com/uc?export=download&id=1JbI2NKFlFF9HLahi0xGBJzM_AQmZtibe",
            image = "https://photo-resize-zmp3.zmdcdn.me/w480_r1x1_webp/cover/c/6/d/e/c6def069a1a885c41fe479358fa7c506.jpg",
            lyrics = "[00:20.22] Mùi hương hoa diên vĩ\n" +
                    "[00:21.72] Hay là hương tóc mềm\n" +
                    "[00:24.49] Ngàn vì sao chẳng sáng\n" +
                    "[00:26.25] Hơn đôi mắt của em\n" +
                    "[00:29.25] Thật đẹp đến trăng thẹn thùng\n" +
                    "[00:31.26] Phải nấp sau mây\n" +
                    "[00:34.30] Vậy thì cớ sao\n" +
                    "[00:35.29] Anh đây lại nỡ buông tay\n" +
                    "[00:38.57] Ừ thì anh không muốn\n" +
                    "[00:40.33] Phải nhìn thấy em buồn\n" +
                    "[00:43.35] Chuyện tình yêu nếu như\n" +
                    "[00:45.10] Không thể nắm thì buông\n" +
                    "[00:48.63] Tự nhiên không biết vì lẽ gì\n" +
                    "[00:51.14] Cứ lại tự kiềm lòng suốt thôi\n" +
                    "[00:57.93] Cây không muốn lá rời cành\n" +
                    "[00:59.68] Khi lá vẫn còn xanh\n" +
                    "[01:02.44] Yêu đương khó quá\n" +
                    "[01:03.70] Thì chạy về khóc với anh\n" +
                    "[01:06.71] Một người luôn yêu em nhất\n" +
                    "[01:08.97] Chắc chắn sẽ không bỏ đi\n" +
                    "[01:10.99] Khiến em phải buồn được đâu\n" +
                    "[01:16.76] Phong ba sóng gió phủ đầu\n" +
                    "[01:18.78] Anh vẫn đứng đằng sau\n" +
                    "[01:21.29] Tình yêu có duyên\n" +
                    "[01:22.56] Thì tự tìm đến với nhau\n" +
                    "[01:25.58] Dù sao anh cũng vui lòng\n" +
                    "[01:27.86] Làm người anh trai\n" +
                    "[01:28.86] Đến suốt đời này được không\n" +
                    "[01:51.94] Ừ thì cây chỉ muốn\n" +
                    "[01:53.44] Giữ chặt lá bên đời\n" +
                    "[01:55.95] Từng sợ một cơn gió kia\n" +
                    "[01:57.98] Sẽ đi đến dạo chơi\n" +
                    "[02:01.68] Sợ rằng cơn gió chợt vô tình\n" +
                    "[02:04.20] Cây ngậm ngùi phải nhìn lá rơi\n" +
                    "[02:10.73] Cây không muốn lá rời cành\n" +
                    "[02:12.73] Khi lá vẫn còn xanh\n" +
                    "[02:15.50] Yêu đương khó quá\n" +
                    "[02:16.50] Thì chạy về khóc với anh\n" +
                    "[02:19.77] Một người luôn yêu em nhất\n" +
                    "[02:22.02] Chắc chắn sẽ không bỏ đi\n" +
                    "[02:23.78] Khiến em phải buồn được đâu\n" +
                    "[02:29.81] Phong ba sóng gió phủ đầu\n" +
                    "[02:31.56] Anh vẫn đứng đằng sau\n" +
                    "[02:34.32] Tình yêu có duyên\n" +
                    "[02:35.32] Thì tự tìm đến với nhau\n" +
                    "[02:38.58] Dù sao anh cũng vui lòng\n" +
                    "[02:40.84] Làm người anh trai\n" +
                    "[02:41.85] Đến suốt đời này được không\n" +
                    "[02:48.61] Cây không muốn lá rời cành\n" +
                    "[02:50.37] Khi lá vẫn còn xanh\n" +
                    "[02:53.13] Yêu đương khó quá\n" +
                    "[02:54.14] Thì chạy về khóc với anh\n" +
                    "[02:57.39] Một người luôn yêu em nhất\n" +
                    "[02:59.40] Chắc chắn sẽ không bỏ đi\n" +
                    "[03:01.42] Khiến em phải buồn được đâu\n" +
                    "[03:07.18] Phong ba sóng gió phủ đầu\n" +
                    "[03:09.20] Anh vẫn đứng đằng sau\n" +
                    "[03:11.95] Tình yêu có duyên\n" +
                    "[03:12.96] Thì tự tìm đến với nhau\n" +
                    "[03:16.22] Dù sao anh cũng vui lòng\n" +
                    "[03:18.22] Làm người anh trai\n" +
                    "[03:19.48] Đến suốt đời này được không\n" +
                    "[03:26.01] Dù sao anh cũng đây rồi\n" +
                    "[03:28.01] Làm người theo em\n" +
                    "[03:29.26] Đến hết đời này mà thôi",
            description = "",
            dateCreated = "01/01/2020",
            songStatus = 0,
            like = 15,
            listen = 5,
            account = accounts[3],
            album = null,
            loveStatus = false,
            singers = arrayListOf(
                accounts[3]
            )
        ),
        Song(
            idSong = 2,
            name = "Đám cưới nha",
            link = "https://docs.google.com/uc?export=download&id=13upaViJZWzSYIakTYZUTZxoJs6VYagzR",
            image = "https://photo-resize-zmp3.zmdcdn.me/w480_r1x1_webp/cover/5/b/8/b/5b8b7cd3d1434afa3b2b9854efdc8756.jpg",
            lyrics = "[00:16.25] Trai tài gái sắc\n" +
                    "[00:18.01] Đôi mình là nhất\n" +
                    "[00:19.76] Làng trên xóm dưới\n" +
                    "[00:21.28] Đẹp đôi thì có ai bằng\n" +
                    "[00:23.78] Yêu nhau mấy mùa trăng\n" +
                    "[00:25.23] Đến lúc tính chuyện trăm năm\n" +
                    "[00:26.99] Chuyến này anh quyết\n" +
                    "[00:28.74] Cưới em liền tay này\n" +
                    "[00:31.00] Đàn trai bưng mâm\n" +
                    "[00:32.00] Theo sau anh rước em về làm dâu\n" +
                    "[00:34.27] Ngày lành trong tháng\n" +
                    "[00:35.27] Trầu cau anh sang\n" +
                    "[00:36.27] Đeo tay em chiếc nhẫn vàng\n" +
                    "[00:38.03] Ôi vui quá xá em ơi\n" +
                    "[00:40.49] Rượu mừng mình nâng ly\n" +
                    "[00:41.75] Ngày mình vu quy\n" +
                    "[00:42.76] Thì ngần ngại chi 1 2 3 dzô hết ly\n" +
                    "[00:45.52] Kiệu vàng ngựa ô\n" +
                    "[00:46.52] Anh khớp đón em về dinh rồi\n" +
                    "[00:49.29] Pháo bay nổ tung trời\n" +
                    "[00:51.05] Cùng xây giấc mơ tuyệt vời\n" +
                    "[00:53.00] Năm sau anh quyết mà làm giàu\n" +
                    "[00:54.76] Cho em sung sướng đến mai sau\n" +
                    "[00:56.78] Đừng lo sóng gió\n" +
                    "[00:57.55] Chuyện gì mà khó thì cứ để đó anh lo\n" +
                    "[01:17.62] Trai tài gái sắc\n" +
                    "[01:19.64] Đôi mình là nhất\n" +
                    "[01:21.39] Làng trên xóm dưới\n" +
                    "[01:22.65] Đẹp đôi thì có ai bằng\n" +
                    "[01:25.16] Yêu nhau mấy mùa trăng\n" +
                    "[01:26.67] Đến lúc tính chuyện trăm năm\n" +
                    "[01:28.93] Chuyến này anh quyết\n" +
                    "[01:30.18] Cưới em liền tay này\n" +
                    "[01:32.18] Đàn trai bưng mâm\n" +
                    "[01:33.20] Theo sau anh rước em về làm dâu\n" +
                    "[01:35.71] Ngày lành trong tháng\n" +
                    "[01:36.72] Trầu cau anh sang\n" +
                    "[01:37.72] Đeo tay em chiếc nhẫn vàng\n" +
                    "[01:39.48] Ôi vui quá xá em ơi\n" +
                    "[01:41.23] Rượu mừng mình nâng ly\n" +
                    "[01:43.25] Ngày mình vu quy\n" +
                    "[01:44.25] Thì ngần ngại chi 1 2 3 dzô hết luôn\n" +
                    "[01:47.01] Kiệu vàng ngựa ô\n" +
                    "[01:48.01] Anh khớp đón em về dinh rồi\n" +
                    "[01:50.73] Pháo bay nổ tung trời\n" +
                    "[01:52.47] Cùng xây giấc mơ tuyệt vời\n" +
                    "[01:54.43] Năm sau anh quyết mà làm giàu\n" +
                    "[01:56.19] Cho em sung sướng đến mai sau\n" +
                    "[01:57.95] Đừng lo sóng gió\n" +
                    "[01:59.16] Chuyện gì mà khó thì cứ để đó anh lo\n" +
                    "[02:02.17] Ví dầu cầu ván mà đóng đinh\n" +
                    "[02:04.88] Anh sẽ đưa em đi về dinh\n" +
                    "[02:06.65] Anh hứa yêu em mãi chung tình\n" +
                    "[02:09.44] Cầu tre gập ghềnh mà khó đi\n" +
                    "[02:12.21] Anh sẽ dắt tay em dìu đi\n" +
                    "[02:13.96] Ôi vui quá xá từ đây hết ế\n" +
                    "[02:16.48] Kiệu vàng ngựa ô\n" +
                    "[02:17.77] Anh khớp đón em về dinh rồi\n" +
                    "[02:20.03] Pháo bay nổ tung trời\n" +
                    "[02:22.04] Cùng xây giấc mơ tuyệt vời\n" +
                    "[02:24.05] Năm sau anh quyết mà làm giàu\n" +
                    "[02:26.05] Cho em sung sướng đến mai sau\n" +
                    "[02:27.80] Đừng lo sóng gió\n" +
                    "[02:28.82] Chuyện gì mà khó thì cứ để đó anh lo",
            description = "",
            dateCreated = "01/01/2020",
            songStatus = 0,
            like = 14,
            listen = 5,
            account = accounts[1],
            album = null,
            loveStatus = false,
            singers = arrayListOf(
                accounts[1],
                accounts[2]
            )
        ),
        Song(
            idSong = 3,
            name = "Vui lắm nha",
            link = "https://docs.google.com/uc?export=download&id=1nqFPNpixzTfCp2mO8Sg_M2nfYA0VOl6W",
            image = "https://photo-resize-zmp3.zmdcdn.me/w480_r1x1_webp/cover/d/2/0/3/d203dca5fe9fbb2190b3b9b729936c53.jpg",
            lyrics = "[00:15.99] Nhớ, đã hứa sẽ cưới rồi nha\n" +
                    "[00:18.50] Hết năm nay mà không thấy\n" +
                    "[00:20.00] Tui mách ba, ba mắng anh giờ\n" +
                    "[00:23.54] Tới tối thức giấc lóng ngóng chờ ai\n" +
                    "[00:25.81] Mong một mai anh tới\n" +
                    "[00:27.57] Mang theo điệu hò đưa dâu\n" +
                    "[00:30.84] Mai đây ta về chung nhà\n" +
                    "[00:32.85] Em sẽ lo trồng rau\n" +
                    "[00:34.60] Nấu những bát cơm chiều\n" +
                    "[00:36.11] Chờ anh thế thôi\n" +
                    "[00:37.56] Trên dưới vui mừng\n" +
                    "[00:38.81] Ngày mình cùng chung đôi\n" +
                    "[00:41.01] Là ngày 25 ta uống rượu giao bôi\n" +
                    "[00:44.98] Mình ở bên nhau\n" +
                    "[00:46.24] Cuộc đời vui lắm nha\n" +
                    "[00:48.75] Cột dây tơ duyên\n" +
                    "[00:50.00] Hai đứa thương tới già\n" +
                    "[00:52.51] Anh nói mai này\n" +
                    "[00:53.77] Em sẽ no ấm cùng anh\n" +
                    "[00:56.03] Hỡi khoang hò khoang\n" +
                    "[00:57.53] Em nguyện yêu thật lòng\n" +
                    "[00:59.80] Mình ở bên nhau\n" +
                    "[01:01.30] Đời lại vui biết bao\n" +
                    "[01:03.56] Cột dây tơ duyên\n" +
                    "[01:05.06] Hai đứa thương quá trời\n" +
                    "[01:07.33] Em đã đem lòng\n" +
                    "[01:08.84] Yêu lấy anh hỡi chàng ơi\n" +
                    "[01:11.10] Lỡ mai về xa anh chờ em theo với\n" +
                    "[01:46.67] Đón em về làng\n" +
                    "[01:47.42] Đám cưới như ngày hội\n" +
                    "[01:48.42] Bò, dê, heo quay, gà dặm hỏi\n" +
                    "[01:51.12] Năm trăm mâm bàn đầy mồi\n" +
                    "[01:52.38] Đồ ăn vừa nóng vừa thổi\n" +
                    "[01:54.41] Xe chở cả hàng đoàn\n" +
                    "[01:55.92] Theo anh về em sẽ không còn cực khổ\n" +
                    "[01:57.94] Nói là làm do cái tính anh đàng hoàng\n" +
                    "[01:59.95] Pháo hoa bấm nút là nổ\n" +
                    "[02:02.40] Vòng kiềng vàng bạc nặng cổ\n" +
                    "[02:06.11] Người chúc đến hàng ngàn\n" +
                    "[02:07.36] Cúi đầu ba lạy quỳ trước bàn thờ tổ\n" +
                    "[02:09.87] Cho em làm bà hoàng\n" +
                    "[02:10.87] Xe nào em thích đỗ đâu thì đỗ\n" +
                    "[02:13.57] Em hỏa anh thì thổ\n" +
                    "[02:14.82] Nhà là phải có ruby bì hổ\n" +
                    "[02:17.08] Nhớ, đã hứa sẽ cưới rồi nha\n" +
                    "[02:19.53] Hết năm nay mà không thấy\n" +
                    "[02:21.29] Tui mách ba, ba mắng anh giờ\n" +
                    "[02:24.50] Tới tối thức giấc lóng ngóng chờ ai\n" +
                    "[02:26.77] Mong một mai anh tới\n" +
                    "[02:28.54] Mang theo điệu hò đưa dâu\n" +
                    "[02:32.06] Mai đây ta về chung nhà\n" +
                    "[02:33.82] Em sẽ lo trồng rau\n" +
                    "[02:35.58] Nấu những bát cơm chiều\n" +
                    "[02:36.85] Chờ anh thế thôi\n" +
                    "[02:38.35] Trên dưới vui mừng\n" +
                    "[02:39.61] Ngày mình cùng chung đôi\n" +
                    "[02:42.05] Là ngày 25 ta uống rượu giao bôi\n" +
                    "[02:45.82] Mình ở bên nhau\n" +
                    "[02:47.08] Cuộc đời vui lắm nha\n" +
                    "[02:49.59] Cột dây tơ duyên\n" +
                    "[02:50.85] Hai đứa thương tới già\n" +
                    "[02:53.38] Anh nói mai này\n" +
                    "[02:54.63] Em sẽ no ấm cùng anh\n" +
                    "[02:56.90] Hỡi khoang hò khoang\n" +
                    "[02:58.40] Em nguyện yêu thật lòng\n" +
                    "[03:00.67] Mình ở bên nhau\n" +
                    "[03:02.18] Đời lại vui biết bao\n" +
                    "[03:04.45] Cột dây tơ duyên\n" +
                    "[03:05.96] Hai đứa thương quá trời\n" +
                    "[03:08.23] Em đã đem lòng\n" +
                    "[03:09.74] Yêu lấy anh hỡi chàng ơi\n" +
                    "[03:12.00] Lỡ mai về xa anh chờ em theo với\n" +
                    "[03:47.13] Hai đứa phu thê\n" +
                    "[03:49.89] Tát cạn khô nước biển đông dạt dào\n" +
                    "[03:54.42] Ra thăm ruộng đồng\n" +
                    "[03:55.92] Ruộng bao la lúa thơm\n" +
                    "[03:57.94] Là em thương bấy nhiêu anh hỡi\n" +
                    "[04:00.71] Mình ở bên nhau\n" +
                    "[04:02.21] Cuộc đời vui lắm nha\n" +
                    "[04:04.47] Cột dây tơ duyên\n" +
                    "[04:05.98] Hai đứa thương tới già\n" +
                    "[04:08.24] Anh nói mai này\n" +
                    "[04:09.76] Em sẽ no ấm cùng anh\n" +
                    "[04:12.03] Hỡi khoang hò khoang\n" +
                    "[04:13.29] Em nguyện yêu thật lòng\n" +
                    "[04:15.80] Mình ở bên nhau\n" +
                    "[04:17.30] Đời lại vui biết bao\n" +
                    "[04:19.31] Cột dây tơ duyên\n" +
                    "[04:20.83] Hai đứa thương quá trời\n" +
                    "[04:23.10] Em đã đem lòng\n" +
                    "[04:24.60] Yêu lấy anh hỡi chàng ơi\n" +
                    "[04:26.86] Lỡ mai về xa anh chờ em theo với",
            description = "",
            dateCreated = "01/01/2020",
            songStatus = 0,
            like = 34,
            listen = 5,
            account = accounts[4],
            album = null,
            loveStatus = false,
            singers = arrayListOf(
                accounts[4],
                accounts[5]
            )
        ),
        Song(
            idSong = 4,
            name = "Không trọn vẹn nữa",
            link = "https://docs.google.com/uc?export=download&id=1ElbjDkQXZjTUFiB_Cpr9H84htotMqrnU",
            image = "https://photo-resize-zmp3.zmdcdn.me/w480_r1x1_webp/cover/7/d/7/c/7d7ccc9ef92fe30ab57543b978ab3548.jpg",
            lyrics = "[00:41.59] Con đường hạnh phúc\n" +
                    "[00:43.09] Đôi ta từng bước qua\n" +
                    "[00:46.83] Cũng đã đến lúc\n" +
                    "[00:49.08] Kết thúc em à\n" +
                    "[00:52.84] Em đã thay đổi đã lừa dối\n" +
                    "[00:55.83] Tình cảm bấy lâu\n" +
                    "[00:58.08] Có lẽ thâm tâm\n" +
                    "[00:59.96] Em chẳng muốn vậy đâu\n" +
                    "[01:03.71] Anh thấy nhớ em nhớ vòng tay\n" +
                    "[01:06.70] Nhớ khoảnh khắc từng đắm say\n" +
                    "[01:09.33] Nhớ những chiều mưa\n" +
                    "[01:11.21] Vui đùa dưới mái tranh êm dịu bình yên\n" +
                    "[01:15.34] Thương em nhiều lắm\n" +
                    "[01:16.84] Thương tấm thân gầy mòn xơ xác lụi tàn\n" +
                    "[01:20.58] Thấy em đớn đau\n" +
                    "[01:22.83] Anh đâu chịu được nổi\n" +
                    "[01:25.82] Hôm qua em còn nơi đó\n" +
                    "[01:28.45] Hôm nay tan về nơi đâu\n" +
                    "[01:31.45] Anh chơi vơi giữa đêm thâu\n" +
                    "[01:33.72] Hỡi thế gian sao lắm u sầu\n" +
                    "[01:37.07] Cô ấy là cả thế giới\n" +
                    "[01:39.71] Là cả bầu trời tương lai\n" +
                    "[01:42.70] Mai này chẳng còn một ai\n" +
                    "[01:44.96] Bên cạnh anh lắng lo mỗi ngày\n" +
                    "[01:48.32] Kiếp này cho anh xin lỗi\n" +
                    "[01:50.96] Chẳng thể bước đi cùng em\n" +
                    "[01:53.95] Đi hết cuộc đời để xem\n" +
                    "[01:56.21] Mối lương duyên liệu có an bài\n" +
                    "[01:59.57] Tình yêu không trọn vẹn nữa\n" +
                    "[02:02.21] Anh đem cất giấu vào tim\n" +
                    "[02:05.20] Kiếp sau có duyên gặp lại\n" +
                    "[02:07.46] Anh chẳng để lạc mất em đâu\n" +
                    "[02:34.46] Anh thấy nhớ em nhớ vòng tay\n" +
                    "[02:37.09] Nhớ khoảnh khắc từng đắm say\n" +
                    "[02:39.70] Nhớ những chiều mưa\n" +
                    "[02:41.58] Vui đùa dưới mái tranh êm dịu bình yên\n" +
                    "[02:45.34] Thương em nhiều lắm\n" +
                    "[02:47.21] Thương tấm thân gầy mòn xơ xác lụi tàn\n" +
                    "[02:50.95] Thấy em đớn đau\n" +
                    "[02:53.21] Anh đâu chịu được nổi\n" +
                    "[02:55.82] Hôm qua em còn nơi đó\n" +
                    "[02:58.84] Hôm nay tan về nơi đâu\n" +
                    "[03:01.83] Anh chơi vơi giữa đêm thâu\n" +
                    "[03:04.08] Hỡi thế gian sao lắm u sầu\n" +
                    "[03:07.46] Cô ấy là cả thế giới\n" +
                    "[03:10.09] Là cả bầu trời tương lai\n" +
                    "[03:13.08] Mai này chẳng còn một ai\n" +
                    "[03:15.33] Bên cạnh anh lắng lo mỗi ngày\n" +
                    "[03:18.71] Kiếp này cho anh xin lỗi\n" +
                    "[03:21.34] Chẳng thể bước đi cùng em\n" +
                    "[03:24.33] Đi hết cuộc đời để xem\n" +
                    "[03:26.58] Mối lương duyên liệu có an bài\n" +
                    "[03:29.96] Tình yêu không trọn vẹn nữa\n" +
                    "[03:32.59] Anh đem cất giấu vào tim\n" +
                    "[03:35.58] Kiếp sau có duyên gặp lại\n" +
                    "[03:38.21] Anh chẳng để lạc mất em đâu\n" +
                    "[03:41.21] Hôm qua em còn nơi đó\n" +
                    "[03:43.84] Hôm nay tan về nơi đâu\n" +
                    "[03:46.83] Anh chơi vơi giữa đêm thâu\n" +
                    "[03:49.08] Hỡi thế gian sao lắm u sầu\n" +
                    "[03:52.46] Cô ấy là cả thế giới\n" +
                    "[03:55.45] Là cả bầu trời tương lai\n" +
                    "[03:58.08] Mai này chẳng còn một ai\n" +
                    "[04:00.63] Bên cạnh anh để lo lắng\n" +
                    "[04:03.64] Kiếp này cho anh xin lỗi\n" +
                    "[04:06.64] Chẳng thể bước đi cùng em\n" +
                    "[04:09.63] Đi hết cuộc đời để xem\n" +
                    "[04:11.88] Mối lương duyên liệu có an bài\n" +
                    "[04:14.89] Tình yêu không trọn vẹn nữa\n" +
                    "[04:17.89] Anh đem cất giấu vào tim\n" +
                    "[04:20.89] Kiếp sau có duyên gặp lại\n" +
                    "[04:23.13] Anh chẳng để lạc mất em đâu\n" +
                    "[04:29.43] Kiếp sau có duyên gặp lại\n" +
                    "[04:31.67] Anh chẳng để lạc mất em đâu",
            description = "",
            dateCreated = "01/01/2020",
            songStatus = 0,
            like = 21,
            listen = 5,
            account = accounts[6],
            album = null,
            loveStatus = false,
            singers = arrayListOf(
                accounts[6]
            )
        ),

        )
}