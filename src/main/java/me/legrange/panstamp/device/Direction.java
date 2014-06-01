/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.legrange.panstamp.device;

/**
 *
 * @author gideon
 */
 enum Direction {
    IN("inp"), OUT("out");

    static Direction forTag(String tag) {
        for (Direction dir : Direction.values()) {
            if (dir.tag.equals(tag)) {
                return dir;
            }
        }
        return null;
    }

    private Direction(String tag) {
        this.tag = tag;
    }
    private final String tag;
    
}
