package com.zuhriddin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zuhriddin.model.LIke;
import com.zuhriddin.service.util.JsonUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LikeService implements BaseService<LIke, UUID> {
    private static final String PATH = "";
    List<LIke> lIkes;

    @Override
    public LIke add(LIke lIke) {
        if (!hasLike(lIke, lIkes)) {
            lIkes.add(lIke);
            write(lIkes);
            return lIke;
        }
        throw new RuntimeException("Like is already exist! ");
    }

    private boolean hasLike(LIke lIke, List<LIke> lIkes) {
        return lIkes.stream()
                .anyMatch(like -> like.getUserID().equals(lIke.getUserID()) &&
                        like.getPostID().equals(like.getPostID()));
    }

    @Override
    public LIke get(UUID id) {
        return read().stream()
                .filter(m -> m.getID().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Like not found!"));
    }

    @Override
    public List<LIke> list() {
        return read();
    }

    @Override
    public void delete(UUID id) {
        List<LIke> likeList = read();
        likeList.stream()
                .filter(m -> m.getID().equals(id))
                .findFirst()
                .ifPresent(likeList::remove);
        write(likeList);
    }

    public List<LIke> likeListPostId(UUID postID) {
        List<LIke> likes = read();
        return likes.stream()
                .filter(like -> postID.equals(like.getPostID()))
                .collect(Collectors.toList());
    }

    private List<LIke> read() {
        return JsonUtil.read(PATH, new TypeReference<>() {
        });
    }

    private void write(List<LIke> lIkes) {
        JsonUtil.write(PATH, lIkes);
    }
}
