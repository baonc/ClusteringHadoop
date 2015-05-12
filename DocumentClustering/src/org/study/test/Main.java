package org.study.test;


public class Main {
	public static void main(String args[]) {
		/*String doc1 = "like_rồng=0.006730430 liên_hệ=0.005558680 kỹ_thuật_số=0.01802730 lienhe=0.00576650 loa=0.030453130 hãng=0.005323730 "
				+ "hương=0.004851810 hàng=0.002753840 home=0.00542270 khách_vãng_lai=0.007853670 hệ_thống=0.004802620 kem=0.005105890 honda=0.005585680 "
				+ "khám_phá=0.005647250 hcm=0.014345960 iphone=0.010116360 han=0.008111260 hà_nội=0.005913850 hapulico_complex=0.006889880 "
				+ "karaoke=0.009142320 quoc=0.007956730 quy_định=0.004450340 quạt_phun_sương=0.005925510 quận_thanh_xuân=0.007051570 quyền=0.004793680 "
				+ "quảng_cáo=0.002840350 qlpt=0.005711620 phước=0.005677490 rao=0.005529460 quy_chế=0.005582490 nổi_bật=0.002807180 "
				+ "nhà_đất_hải_phòng=0.005773910 nhà_đất_bình_dương=0.005773910 phong_thủy=0.005625780 nhiều=0.001408380 nokia=0.005503140 "
				+ "nội_dung=0.00290670 nhà_đất_nam_định=0.005773910 nắng=0.004621210 phong=0.004908510 nhà=0.002280040 năm=0.001290020 "
				+ "nguyễn_huy_tưởng=0.006781910 ngày=0.003329140 mỗi=0.002803680 mới=0.000968880 map=0.005736370 mp5=0.018369490 mini=0.005446620 "
				+ "mua_bán=0.005159070 mua=0.01673790 mạng=0.00277950 mp4=0.008740770 mp3=0.006799820 làm=0.001410440 mặt=0.003117960 lumia=0.005658630 "
				+ "máy=0.003230490 lúc=0.002752460 mã_tin=0.006596350 mua_bán_nhà_đất=0.005773910 việc_làm_đà_nẵng=0.005925510 việc_làm=0.004942690 "
				+ "voice=0.024367840 việt_nam_giấy=0.005772740 vay=0.005041020 tìm_việc_làm_thêm=0.005773910 tower=0.00573560 trực_tuyến=0.002977240 "
				+ "tín_chấp=0.005765330 tp_hcm=0.006840230 tòa=0.004670090 tro=0.005384350 tân=0.005216690 tuyển_dụng=0.005576140 truyền_thông=0.004231670 "
				+ "tải=0.002686290 trợ_giảng=0.014630120 tranh=0.004305040 trả_góp=0.005692420 tầng=0.003852220 chứng_nhận=0.005263160 chống=0.003833190 "
				+ "com=0.005739660 city=0.005459510 chữ_thập=0.005743280 chuyên=0.005476180 blog=0.004412440 chung_cư=0.01638950 center_building=0.006794490 "
				+ "bay=0.008916020 bản_mobile=0.005773130 ampli_pioneer=0.005925510 cho=0.004072820 app_store=0.005757180 afamily=0.005534630 "
				+ "bạn=0.001005520 bán=0.013141860 chuyên_mục=0.003195640 acoustic=0.03826540 anh=0.005266060 energy=0.040783070 căn_hộ=0.016050570 "
				+ "copyright=0.003628410 cung_cấp=0.003958020 diễn_đàn_mua_rẻ=0.005773910 dữ_liệu=0.004756270 cổ_phần=0.002738640 cục=0.004651690 "
				+ "cập_nhật=0.004253160 cấp=0.003931970 du_lịch=0.004741140 container=0.005787240 công_ty=0.002263120 cskh=0.005369730 dịch_vụ=0.00435220 "
				+ "cú_pháp=0.006593360 cực=0.008307840 ext=0.005290010 fax=0.005190840 gamek=0.005611370 giao_diện=0.004290360 giấy=0.004460970 "
				+ "gxn=0.003897610 gặp=0.003506440 giá=0.022906630 đăng=0.013176620 vặt=0.005623610 xác_nhận=0.005153970 điện_tử=0.009075750 "
				+ "xe_toyota=0.005773130 xem=0.002321590 xe_máy=0.009988710 vị_trí=0.00453070 điều_hòa=0.005571560 đà_nẵng=0.005236980 "
				+ "điện_thoại=0.003509090 xinh=0.007348430 xã_hội=0.003079870 đầu_tư=0.004191110 ủy_ban_nhân_dân=0.005716540 đất=0.003956660 "
				+ "ưu_tiên=0.005224310 sunrise_city=0.005770780 sim=0.00496360 spring=0.005746740 sms=0.005942930 site=0.005679350 sửa=0.004940410 "
				+ "rửa=0.004696680 rss=0.00282850 tdđt=0.005773130 thuê=0.009524530 royal=0.005744050 thêm=0.001805490 thu_đông=0.005748290 "
				+ "rongbay=0.023065990 rồng=0.005900720 rồng_bay=0.005773130 thue=0.005592070 sinh_viên=0.004770360 samsung_galaxy_note=0.005747520 "
				+ "tháng=0.001766680 tin=0.010011030 thời_trang=0.008836450 thêu=0.005610650";
		String doc2 = "thời_đại=0.011259650 thông_tin=0.006998860 sữa=0.009503730 sub_tiếng_việt=0.016245430 text=0.011134480 "
				+ "subviet_trang=0.015994430 sở_hữu=0.007743590 sau=0.0025970 rights=0.011011910 sưu_tầm=0.008387930 sub=0.036394930 reserved=0.007962340 "
				+ "theo_dõi=0.006984190 trang=0.005839470 truyensubviet=0.063661160 tiếng=0.014894990 truyện_hay=0.01595460 trải=0.008640830 "
				+ "truyện_tranh_hot_tổng=0.015915290 truyện_yêu_thích=0.01595460 truyện_hot_truyện_yêu_thích_truyện_hay_truyện_hot_bạn=0.018664970 "
				+ "trách_nhiệm=0.007038510 truyện_tranh_hay_tổng=0.015915290 truyện_tranh_yêu_thích_tổng=0.015915290 truyện=0.049862670 "
				+ "truyện_hot_chọn=0.01595460 truyện_tranh_hoàn_thành_tổng=0.015915290 tìm_kiếm=0.005762840 truyện_tranh=0.008049590 tặng=0.007677990 "
				+ "việt=0.017633140 tổ_chức=0.005978470 nào=0.008764720 năm_tháng=0.012985180 nhóe=0.015928330 nhât=0.015430270 nhanh_nhất=0.011342920 "
				+ "online=0.00468970 nhóm=0.006959910 nhiều=0.004330540 này=0.001455940 qua=0.006628020 quà=0.009071850 quan_tâm=0.006668860 hot=0.017106770 "
				+ "khi=0.004070980 internet=0.008206250 hợp=0.035923380 hình_ảnh=0.00531090 hay=0.01364220 không=0.001746950 link=0.018184930 kia=0.00736880 "
				+ "kingdom=0.048206660 liên_hệ=0.0085460 liên_kết=0.016021580 mình=0.002728920 lên=0.005501350 lỗi=0.006800670 mọi=0.008264940 "
				+ "làm=0.002168430 mỗi=0.004310420 ngày=0.001706090 next_chap=0.016001120 ảnh_hưởng=0.006900220 đọc=0.004356110 đến=0.003122440 "
				+ "đầu=0.003350990 đặt=0.0048880 được=0.004231650 website=0.005089550 điều=0.00471030 đánh_giá=0.00691410 đáng=0.006221830 xem=0.003569250 "
				+ "xem_xét=0.009300640 web=0.005594550 yêu_cầu=0.005780910 bình_luận=0.006320320 bạn_gái=0.009920280 cho=0.001252320 all=0.006233840 "
				+ "chap=0.022346870 chapter=0.014305050 bạn=0.006183630 chủ=0.006957080 com=0.008824230 chào_mừng=0.007381810 click=0.006900840 "
				+ "chịu=0.006038780 gửi=0.004845020 giáng_sinh=0.010981130 full=0.008432570 group_truyen=0.015994430 cập_nhật=0.006538870 "
				+ "cá_nhân=0.007027560 dịch=0.010580820 copyright=0.005578370 facebook=0.006590240 dammetruyen=0.01314130";
		String doc1Array[] = doc1.trim().split(" ");
		String doc2Array[] = doc2.trim().split(" ");
		
		double distance = Distance.distance(doc1Array, doc2Array);
		System.out.println("Distance = " + distance);
		
		String doc[][] = new String[3][];
		doc[0] = doc1.trim().split(" ");
		String docArray[] = doc2.trim().split(" ");
		doc[1] = docArray;
		doc[2] = null;
		
		String sumDoc[] = null;
		String sum[] = Distance.sum(sumDoc, doc[0]);
		for(int i = 0; i < sum.length; i++) {
			System.out.println(sum[i]);
		}
		
		Test10000 test10 = new Test10000();
		test10.createdFile();*/
		Get1000Doc get1000 = new Get1000Doc();
		get1000.getTop1000();
	}
}
