class Solution {
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        int n = nums.length;
        boolean val = false;
        HashMap<Integer, Integer> hm = new HashMap<>();
        for (int i = 0; i < n; i++) {
            if (hm.containsKey(nums[i])) {
                int prevIdx = hm.get(nums[i]);
                if (i - prevIdx <= k) {
                    return true;
                }

            }
            hm.put(nums[i], i);
        }
        return false;
    }
}