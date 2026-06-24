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

// using sliding window and hashset

class Solution {
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        int n = nums.length;

        HashSet<Integer>hs = new HashSet<Integer>();

        for(int i=0;i<n;i++){
            if(hs.contains(nums[i]))return true;
            hs.add(nums[i]);

            if(hs.size() > k ){
                hs.remove(nums[i -k]);
            }
        }
        return false;
    }
}