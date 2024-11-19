package com.example.smartrecyclingapp;

import java.util.ArrayList;
import java.util.List;

public class RecyclingData {
    public static List<LocationMap> getLocationsByWasteType(String wasteType) {
        List<LocationMap> locations = new ArrayList<>();

        switch (wasteType) {
            case "Nhựa":
                locations.add(new LocationMap("Trạm tái chế và thu mua Nhựa Mưa Thuận Q.12", 10.844046, 106.630966));
                locations.add(new LocationMap("Trạm tái chế,đổi quà lấy Nhựa  UBND Q.7 ", 10.732325, 106.726648));
                locations.add(new LocationMap("Trạm thu gom,tái chế nhựa cá nhân chị Hồng Q.8", 10.738709, 106.662292));
                locations.add(new LocationMap("Trạm thu gom,tái chế nhựa và phế liệu 24h (5 sao) Q.Tân Bình", 10.804689, 106.638144));

                break;

            case "Kim loại":
                locations.add(new LocationMap("Trạm tái chế kim loại UBND Q.9", 10.7815283, 106.6820161));
                locations.add(new LocationMap("Trạm tái chế kim loại UBND Q.Phú NHuận", 10.7927812, 106.6782158));
                locations.add(new LocationMap("Trạm tái chế kim loại giá cao Hải Đăng Q.Bình Tân", 10.7982787,106.6097537));

                break;

            case "Bìa cứng":
                locations.add(new LocationMap("Trạm thu mua,tái chế chuyên bìa cứng uy tín Phú Cường Hưng Q.2", 10.8095605, 106.729627));
                locations.add(new LocationMap("Trạm thu mua,tái chế chuyên bìa cứng uy tín Phú Cường Hưng Q.12 ", 10.8821916, 106.6693047));
                locations.add(new LocationMap("Trạm thu mua,tái chế chuyên bìa cứng uy tín Phú Cường Hưng Q.Bình Tân", 10.782082, 106.6169079));
                break;

            case "Thủy tinh":
                locations.add(new LocationMap("Trạm tái chế thủy tinh ANNAM GOURMET Saigon Center Q.1 ", 10.7731657, 106.6980589));
                locations.add(new LocationMap("Trạm tái chế thủy tinh  Xanh Cà phê và cơm Q.1", 10.763177, 106.6906544));
                locations.add(new LocationMap("Trạm tái chế thủy tinh Thảo Điền Eco Wellness Q.2 ", 10.8063901, 106.7421288));
                locations.add(new LocationMap("Trạm tái chế thủy tinh ANNAM GOURMET Riverpark Premier Q.7 ", 10.8080503, 106.7003733));
                break;

            case "Giấy":
                locations.add(new LocationMap("Trạm tái chế giấy Quang Phát Q.Bình Chánh ", 10.8314136, 106.5704575));
                locations.add(new LocationMap("Trạm tái chế,thu mua và sản xuất giấy Phú Tùng Q.Tân Phú ", 10.7929113, 106.6363113));
                locations.add(new LocationMap("Trạm thu mua,tái chế chuyên bìa cứng uy tín Phú Cường Hưng Q.2", 10.8095605, 106.729627));
                locations.add(new LocationMap("Trạm thu mua,tái chế chuyên bìa cứng uy tín Phú Cường Hưng Q.12 ", 10.8821916, 106.6693047));
                locations.add(new LocationMap("Trạm thu mua,tái chế chuyên bìa cứng uy tín Phú Cường Hưng Q.Bình Tân", 10.782082, 106.6169079));
                break;

            default:
                // Không tìm thấy loại rác
                break;
        }

        return locations;
    }
}
