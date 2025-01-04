package com.price.model;

import com.roomType.model.RoomTypeRepository;
import com.roomType.model.RoomTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PriceService {
    @Autowired
    private PriceRepository priceRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    // 根據房型ID查詢價格列表
    public List<PriceVO> getPricesByRoomTypeId(Integer roomTypeId) {
        return priceRepository.findByRoomType_RoomTypeId(roomTypeId);
    }

    // 更新房型價格
    public void updatePrices(Integer roomTypeId, List<PriceVO> priceVOs) {
        // 確認房型是否存在
        RoomTypeVO roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new IllegalArgumentException("無效的房型ID：" + roomTypeId));

        // 查詢該房型的所有價格資料
        List<PriceVO> existingPrices = priceRepository.findByRoomType_RoomTypeId(roomTypeId);

        // 遍歷提交的價格數據
        for (PriceVO priceVO : priceVOs) {
            // 根據 priceType 找到對應的現有資料
            PriceVO existingPrice = existingPrices.stream()
                    .filter(p -> p.getPriceType().equals(priceVO.getPriceType()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("找不到對應的價格類型：" + priceVO.getPriceType()));

            // 更新價格和早餐價格
            existingPrice.setPrice(priceVO.getPrice());
            existingPrice.setBreakfastPrice(priceVO.getBreakfastPrice());

            // 保存更新後的價格
            priceRepository.save(existingPrice);
        }
    }

    public void addSpecialPrice(Integer roomTypeId, PriceVO priceVO) {
        // 查找房型
        RoomTypeVO roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new IllegalArgumentException("无效的房型 ID"));

        // 设置 PriceVO 属性
        priceVO.setRoomType(roomType); // 设置房型
        priceVO.setPriceType((byte) 3); // 特殊价格类型

        // 保存到数据库
        priceRepository.save(priceVO);
    }


    public List<PriceVO> findSpecialPricesByRoomTypeIds(List<Integer> roomTypeIds) {
        return priceRepository.findByRoomTypeRoomTypeIdInAndPriceType(roomTypeIds, (byte) 3);
    }

    public void deleteSpecialPrice(Integer priceId) {
        if (!priceRepository.existsById(priceId)) {
            throw new IllegalArgumentException("记录不存在");
        }
        priceRepository.deleteById(priceId);
    }
}
