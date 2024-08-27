package com.zuhriddin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zuhriddin.model.Follow;
import com.zuhriddin.service.util.JsonUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FollowService implements BaseService<Follow, UUID> {
    private static final String PATH = "instagram/resource/Follows.json";


    @Override
    public Follow add(Follow follow) {
        List<Follow> followList = read();
        if (hasFollow(followList, follow)) {
            throw new RuntimeException();
        }
        followList.add(follow);
        write(followList);
        return follow;
    }

    private boolean hasFollow(List<Follow> followList, Follow follow) {
        return followList.stream()
                .anyMatch(n -> n.getFollowingId().equals(follow.getFollowerId()) && n.getFollowerId().equals(follow.getFollowerId()));
    }

    @Override
    public Follow get(UUID id) {
        return read().stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Follow> list() {
        return read();
    }

    @Override
    public void delete(UUID id) {
        List<Follow> followList = read().stream()
                .filter(n -> !n.getId().equals(id))
                .collect(Collectors.toList());
        write(followList);
    }

    public List<Follow> getFollowers(UUID id) {
        return read().stream()
                .filter(n -> n.getFollowingId().equals(id))
                .collect(Collectors.toList());
    }

    public List<Follow> getFollowings(UUID id) {
        return read().stream()
                .filter(n -> n.getFollowerId().equals(id))
                .collect(Collectors.toList());
    }

    private List<Follow> read() {
        return JsonUtil.read(PATH, new TypeReference<>() {});
    }

    private void write(List<Follow> followList) {
        JsonUtil.write(PATH, followList);
    }
}
